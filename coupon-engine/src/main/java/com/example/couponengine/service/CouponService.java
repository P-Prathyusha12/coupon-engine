package com.example.couponengine.service;

import com.example.couponengine.dto.CouponRequest;
import com.example.couponengine.dto.CouponResponse;
import com.example.couponengine.dto.OrderResponse;
import com.example.couponengine.entity.*;
import com.example.couponengine.repository.*;
import com.example.couponengine.strategy.DiscountStrategy;
import com.example.couponengine.strategy.DiscountStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDiscountRepository orderDiscountRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private DiscountStrategyFactory strategyFactory;

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode());
        coupon.setType(request.getType());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon.setMinOrderValue(request.getMinOrderValue());

        Coupon saved = couponRepository.save(coupon);
        return toResponse(saved);
    }

    @Transactional
    public OrderDiscount applyCoupon(Long orderId, String couponCode) {
        // Fetch order with discounts using JOIN FETCH (avoids N+1)
        Order order = orderRepository.findByIdWithDiscounts(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        // Fetch coupon with Pessimistic Write Lock to prevent concurrent usage issues
        Coupon coupon = couponRepository.findByCodeWithLock(couponCode)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found with code: " + couponCode));

        // 1. Validate Expiry
        if (coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Coupon '" + couponCode + "' has expired.");
        }

        // 2. Validate Usage Limit
        if (coupon.getUsageLimit() <= 0) {
            throw new IllegalStateException("Coupon '" + couponCode + "' usage limit has been reached.");
        }

        // 3. Check Idempotency: same coupon on same order
        if (orderDiscountRepository.findByOrderIdAndCouponId(order.getId(), coupon.getId()).isPresent()) {
            throw new IllegalStateException("Coupon '" + couponCode + "' has already been applied to this order.");
        }

        // 4. Check if user already used this coupon (single-use per user)
        userCouponRepository.findByUserIdAndCouponId(order.getUser().getId(), coupon.getId())
                .ifPresent(uc -> {
                    if (Boolean.TRUE.equals(uc.getUsedFlag())) {
                        throw new IllegalStateException("User has already used coupon '" + couponCode + "'.");
                    }
                });

        // 5. Calculate Discount using Strategy Pattern
        DiscountStrategy strategy = strategyFactory.getStrategy(coupon.getType());
        BigDecimal discountAmount = strategy.calculateDiscount(order, coupon);

        if (discountAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Order does not meet the minimum value required for coupon '" + couponCode + "'.");
        }

        // 6. Decrement Usage Limit (safe under pessimistic lock)
        coupon.setUsageLimit(coupon.getUsageLimit() - 1);
        couponRepository.save(coupon);

        // 7. Mark coupon as used for this user
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUser(order.getUser());
        userCoupon.setCoupon(coupon);
        userCoupon.setUsedFlag(true);
        userCouponRepository.save(userCoupon);

        // 8. Save and return discount record
        OrderDiscount orderDiscount = new OrderDiscount();
        orderDiscount.setOrder(order);
        orderDiscount.setCoupon(coupon);
        orderDiscount.setDiscountApplied(discountAmount);
        return orderDiscountRepository.save(orderDiscount);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderDetails(Long orderId) {
        Order order = orderRepository.findByIdWithDiscounts(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        List<OrderResponse.DiscountDetail> details = new ArrayList<>();
        BigDecimal totalDiscount = BigDecimal.ZERO;

        if (order.getDiscounts() != null) {
            for (OrderDiscount od : order.getDiscounts()) {
                details.add(new OrderResponse.DiscountDetail(
                        od.getCoupon().getCode(),
                        od.getDiscountApplied()
                ));
                totalDiscount = totalDiscount.add(od.getDiscountApplied());
            }
        }

        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setUserId(order.getUser().getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setTotalDiscount(totalDiscount);
        response.setFinalAmount(order.getTotalAmount().subtract(totalDiscount));
        response.setDiscountsApplied(details);
        return response;
    }

    private CouponResponse toResponse(Coupon coupon) {
        CouponResponse r = new CouponResponse();
        r.setId(coupon.getId());
        r.setCode(coupon.getCode());
        r.setType(coupon.getType());
        r.setDiscountValue(coupon.getDiscountValue());
        r.setExpiryDate(coupon.getExpiryDate());
        r.setUsageLimit(coupon.getUsageLimit());
        r.setMinOrderValue(coupon.getMinOrderValue());
        return r;
    }
}
