package com.yosep.coupon.coupon.data.jpa.dto

import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import org.springframework.hateoas.RepresentationModel

data class CreatedProductDiscountCouponDto(
    val couponId: String,
    val name: String,
    val couponStockVo: CouponStockVo,
    val productId: String = "",
    val couponDiscountVo: CouponDiscountVo,
    val dtype: String?
)
//    : RepresentationModel<CreatedProductDiscountCouponDto>()