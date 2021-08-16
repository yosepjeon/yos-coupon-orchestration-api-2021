package com.yosep.coupon.coupon.data.jpa.dto

import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class TotalDiscountCouponDtoForCreation(
    var couponId: String,

    @field:NotEmpty
    val name: String,

    @field:NotNull
    val couponStockVo: CouponStockVo,
    @field:NotNull
    val couponDiscountVo: CouponDiscountVo
)