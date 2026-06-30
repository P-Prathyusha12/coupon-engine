package com.example.couponengine.repository;

import com.example.couponengine.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Fetch order with discounts to avoid N+1 queries
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.discounts WHERE o.id = :id")
    Optional<Order> findByIdWithDiscounts(@Param("id") Long id);
}
