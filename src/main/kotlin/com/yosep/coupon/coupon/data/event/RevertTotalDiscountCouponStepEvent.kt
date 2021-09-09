package com.yosep.coupon.coupon.data.event

import com.yosep.coupon.coupon.data.jpa.dto.OrderTotalDiscountCouponDto

data class RevertTotalDiscountCouponStepEvent(
    val eventId: String,
    val orderTotalDiscountCouponDtos: List<OrderTotalDiscountCouponDto>,
) {

}