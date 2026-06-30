package com.example.couponengine.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {
    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscount;
    private BigDecimal finalAmount;
    private List<DiscountDetail> discountsApplied;

    public OrderResponse() {}

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(BigDecimal totalDiscount) { this.totalDiscount = totalDiscount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public List<DiscountDetail> getDiscountsApplied() { return discountsApplied; }
    public void setDiscountsApplied(List<DiscountDetail> discountsApplied) { this.discountsApplied = discountsApplied; }

    public static class DiscountDetail {
        private String couponCode;
        private BigDecimal discountApplied;

        public DiscountDetail() {}
        public DiscountDetail(String couponCode, BigDecimal discountApplied) {
            this.couponCode = couponCode;
            this.discountApplied = discountApplied;
        }

        public String getCouponCode() { return couponCode; }
        public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

        public BigDecimal getDiscountApplied() { return discountApplied; }
        public void setDiscountApplied(BigDecimal discountApplied) { this.discountApplied = discountApplied; }
    }
}
