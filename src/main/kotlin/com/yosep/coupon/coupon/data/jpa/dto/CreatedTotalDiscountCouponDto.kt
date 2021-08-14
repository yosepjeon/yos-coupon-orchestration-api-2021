package com.yosep.coupon.coupon.data.jpa.dto

import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo

data class CreatedTotalDiscountCouponDto(
    val couponId: String,
    val name: String,
    val couponStockVo: CouponStockVo,
    val couponDiscountVo: CouponDiscountVo
)