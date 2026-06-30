package com.example.couponengine.strategy;

import com.example.couponengine.entity.Coupon;
import com.example.couponengine.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("CONDITIONAL")
public class ConditionalDiscountStrategy implements DiscountStrategy {
    
    @Override
    public BigDecimal calculateDiscount(Order order, Coupon coupon) {
        // e.g., if totalAmount > minOrderValue, apply flat discount
        if (coupon.getMinOrderValue() != null && order.getTotalAmount().compareTo(coupon.getMinOrderValue()) >= 0) {
            BigDecimal discount = coupon.getDiscountValue();
            if (discount.compareTo(order.getTotalAmount()) > 0) {
                return order.getTotalAmount();
            }
            return discount;
        }
        // If condition not met, discount is 0
        return BigDecimal.ZERO;
    }
}
