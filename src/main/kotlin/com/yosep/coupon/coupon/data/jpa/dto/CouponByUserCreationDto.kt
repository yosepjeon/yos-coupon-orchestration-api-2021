package com.yosep.coupon.coupon.data.jpa.dto

import javax.validation.constraints.NotEmpty

data class CouponByUserCreationDto(
    var id: String,

    @field:NotEmpty
    val userId: String,

    @field:NotEmpty
    val couponId: String,
)