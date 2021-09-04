package com.yosep.coupon.coupon.data.jpa.dto

//class OrderTotalDiscountCouponDto(
//    dtype: String,
//    couponByUserId: String,
//    productCount: Long,
//    userId: String,
//    discountAmount: Long,
//    discountPercent: Long,
//    totalPrice: Long,
//    calculatedPrice: Long,
//    state: String = "READY"
//) : OrderDiscountCouponDto(
//    dtype,
//    couponByUserId,
//    productCount,
//    userId,
//    discountAmount,
//    discountPercent,
//    totalPrice,
//    calculatedPrice,
//    state
//)

data class OrderTotalDiscountCouponDto(
    val dtype: String,
    val couponByUserId: String,
    val userId: String,
    val discountAmount: Long,
    val discountPercent: Long,
    var state: String = "READY"
)