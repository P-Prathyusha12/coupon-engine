package com.example.couponengine.strategy;

import com.example.couponengine.entity.Coupon;
import com.example.couponengine.entity.Order;
import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculateDiscount(Order order, Coupon coupon);
}
