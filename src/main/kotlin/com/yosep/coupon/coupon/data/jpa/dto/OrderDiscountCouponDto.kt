package com.yosep.coupon.coupon.data.jpa.dto

open class OrderDiscountCouponDto(
    open val dtype: String,
    open val couponByUserId: String,
    open val productCount: Long,
    open val userId: String,
    open val couponType: String,
    open val discountAmount: Long,
    open val discountPercent: Long,
    open val totalPrice: Long,
    open var calculatedPrice: Long,
    open var state: String = "READY"
)