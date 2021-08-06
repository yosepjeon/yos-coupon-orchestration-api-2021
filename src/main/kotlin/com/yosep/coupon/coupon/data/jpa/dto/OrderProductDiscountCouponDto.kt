package com.yosep.coupon.coupon.data.jpa.dto

data class OrderProductDiscountCouponDto(
    val dtype: String,
    val couponByUserId: String,
    val productCount: Long,
    val userId: String,
    val couponType: String,
    val discountAmount: Long,
    val discountPercent: Long,
    val productId: String,
    val totalPrice: Long,
    var calculatedPrice: Long,
    var state: String = "READY"
)