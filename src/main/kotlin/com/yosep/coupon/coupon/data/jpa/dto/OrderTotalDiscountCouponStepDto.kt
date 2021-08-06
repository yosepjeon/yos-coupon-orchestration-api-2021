package com.yosep.coupon.coupon.data.jpa.dto

import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class OrderTotalDiscountCouponStepDto(
    @field:Min(0)
    val totalPrice:Long,

    @field:NotEmpty
    @field:Size(min = 1)
    val orderTotalDiscountCouponDtos: List<OrderTotalDiscountCouponDto>,

    @field:NotEmpty
    val state: String = "READY"
)