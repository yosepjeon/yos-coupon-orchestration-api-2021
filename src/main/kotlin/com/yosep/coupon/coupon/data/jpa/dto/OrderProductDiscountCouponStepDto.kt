package com.yosep.coupon.coupon.data.jpa.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class OrderProductDiscountCouponStepDto(
    @field:NotEmpty
    @field:Size(min = 1)
    val orderProductDiscountCouponDtos: List<OrderProductDiscountCouponDto>,

    @field:NotEmpty
    val state: String = "READY"
)