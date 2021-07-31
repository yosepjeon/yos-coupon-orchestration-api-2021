package com.yosep.coupon.coupon.data.jpa.dto

data class OrderCouponDtoForCreation(
    val couponId: String,
    val count:Long,
    val userId: String,
    val couponType: String,
    val discountAmount: Long,
    val discountPercent: Long,
    val productId: String,
//    val price: Long,
    var state: String = "READY"
)