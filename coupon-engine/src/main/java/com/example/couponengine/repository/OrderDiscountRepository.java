package com.example.couponengine.repository;

import com.example.couponengine.entity.OrderDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderDiscountRepository extends JpaRepository<OrderDiscount, Long> {
    Optional<OrderDiscount> findByOrderIdAndCouponId(Long orderId, Long couponId);
}
