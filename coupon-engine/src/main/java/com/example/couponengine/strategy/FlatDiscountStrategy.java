package com.example.couponengine.strategy;

import com.example.couponengine.entity.Coupon;
import com.example.couponengine.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("FLAT")
public class FlatDiscountStrategy implements DiscountStrategy {
    
    @Override
    public BigDecimal calculateDiscount(Order order, Coupon coupon) {
        BigDecimal discount = coupon.getDiscountValue();
        // Discount cannot be greater than the order total amount
        if (discount.compareTo(order.getTotalAmount()) > 0) {
            return order.getTotalAmount();
        }
        return discount;
    }
}
