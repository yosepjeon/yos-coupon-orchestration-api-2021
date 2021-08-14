package com.yosep.coupon.coupon.data.jpa.dto

import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty

data class TotalDiscountCouponDtoForCreation(
    var couponId: String,

    @field:NotEmpty
    val name: String,

    @field:Min(0)
    val couponStockVo: CouponStockVo,

    val couponDiscountVo: CouponDiscountVo
)