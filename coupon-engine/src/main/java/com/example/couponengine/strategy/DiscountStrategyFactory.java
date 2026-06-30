package com.example.couponengine.strategy;

import com.example.couponengine.entity.CouponType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DiscountStrategyFactory {

    private final Map<String, DiscountStrategy> strategies;

    @Autowired
    public DiscountStrategyFactory(Map<String, DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public DiscountStrategy getStrategy(CouponType type) {
        DiscountStrategy strategy = strategies.get(type.name());
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for coupon type: " + type);
        }
        return strategy;
    }
}
