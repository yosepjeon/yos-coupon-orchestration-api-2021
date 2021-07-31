package com.yosep.coupon.coupon.data.jpa.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class CouponStepDtoForCreation (
    @field:NotEmpty
    @field:Size(min = 1)
    val orderProductDtos: List<OrderCouponDtoForCreation>,

    @field:NotEmpty
    val state: String = "READY"
)