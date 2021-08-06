package com.yosep.coupon.coupon.data.jpa.dto

data class OrderTotalDiscountCouponDto(
    val couponByUserId: String,
    val userId: String,
    val discountAmount: Long,
    val discountPercent: Long,
    var state: String = "READY"
)