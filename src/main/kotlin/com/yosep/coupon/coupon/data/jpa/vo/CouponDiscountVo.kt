package com.yosep.coupon.coupon.data.jpa.vo

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class CouponDiscountVo(
    @field:Min(0)
    val discountAmount: Long,
    @field:Min(0)
    @field:Max(100)
    val discountPercent: Long
)