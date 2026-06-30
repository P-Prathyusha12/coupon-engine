package com.example.couponengine.dto;

import com.example.couponengine.entity.CouponType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponResponse {
    private Long id;
    private String code;
    private CouponType type;
    private BigDecimal discountValue;
    private LocalDateTime expiryDate;
    private Integer usageLimit;
    private BigDecimal minOrderValue;

    public CouponResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public CouponType getType() { return type; }
    public void setType(CouponType type) { this.type = type; }

    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }

    public BigDecimal getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(BigDecimal minOrderValue) { this.minOrderValue = minOrderValue; }
}
