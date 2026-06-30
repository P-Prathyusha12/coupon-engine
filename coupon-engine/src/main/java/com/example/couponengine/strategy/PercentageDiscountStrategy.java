package com.example.couponengine.strategy;

import com.example.couponengine.entity.Coupon;
import com.example.couponengine.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("PERCENTAGE")
public class PercentageDiscountStrategy implements DiscountStrategy {
    
    @Override
    public BigDecimal calculateDiscount(Order order, Coupon coupon) {
        BigDecimal percentage = coupon.getDiscountValue();
        // Assuming percentage is stored as 0-100
        BigDecimal discount = order.getTotalAmount().multiply(percentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        return discount;
    }
}
