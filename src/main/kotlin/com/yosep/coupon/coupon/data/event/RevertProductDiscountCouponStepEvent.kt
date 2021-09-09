package com.yosep.coupon.coupon.data.event

import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto

data class RevertProductDiscountCouponStepEvent(
    val eventId: String,
    val orderProductDiscountCouponDtos: List<OrderProductDiscountCouponDto>
) {
}