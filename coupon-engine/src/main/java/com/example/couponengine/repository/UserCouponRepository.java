package com.example.couponengine.repository;

import com.example.couponengine.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
}
