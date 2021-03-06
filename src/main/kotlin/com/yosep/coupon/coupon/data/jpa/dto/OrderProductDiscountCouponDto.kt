package com.yosep.coupon.coupon.data.jpa.dto

class OrderProductDiscountCouponDto(
    dtype: String,
    couponByUserId: String,
    productCount: Long,
    userId: String,
    discountAmount: Long,
    discountPercent: Long,
    val productId: String,
    totalPrice: Long,
    calculatedPrice: Long,
    state: String = "READY"
) : OrderDiscountCouponDto(
    dtype,
    couponByUserId,
    productCount,
    userId,
    discountAmount,
    discountPercent,
    totalPrice,
    calculatedPrice,
    state
)