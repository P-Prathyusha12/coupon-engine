package com.example.couponengine.controller;

import com.example.couponengine.dto.CouponRequest;
import com.example.couponengine.dto.CouponResponse;
import com.example.couponengine.dto.OrderResponse;
import com.example.couponengine.entity.OrderDiscount;
import com.example.couponengine.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    // CREATE a coupon
    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest request) {
        CouponResponse saved = couponService.createCoupon(request);
        return ResponseEntity.ok(saved);
    }

    // APPLY a coupon to an order
    @PostMapping("/apply")
    public ResponseEntity<String> applyCoupon(@RequestParam Long orderId,
                                              @RequestParam String couponCode) {
        OrderDiscount discount = couponService.applyCoupon(orderId, couponCode);
        return ResponseEntity.ok("Coupon applied successfully! Discount Amount: ₹" + discount.getDiscountApplied());
    }

    // GET order with applied discounts
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        OrderResponse response = couponService.getOrderDetails(orderId);
        return ResponseEntity.ok(response);
    }
}
