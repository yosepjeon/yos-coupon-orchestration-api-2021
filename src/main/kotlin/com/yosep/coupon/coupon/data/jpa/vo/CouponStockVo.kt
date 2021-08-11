package com.yosep.coupon.coupon.data.jpa.vo

import javax.validation.constraints.Min

data class CouponStockVo(
    @field:Min(0)
    val total: Long,
    @field:Min(0)
    val remain: Long
)