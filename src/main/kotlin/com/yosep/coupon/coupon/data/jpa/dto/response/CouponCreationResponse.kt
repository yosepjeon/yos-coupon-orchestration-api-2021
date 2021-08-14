package com.yosep.coupon.coupon.data.jpa.dto.response

import com.yosep.coupon.coupon.data.jpa.dto.CreatedProductDiscountCouponDto
import org.springframework.hateoas.RepresentationModel

class CouponCreationResponse(
    val createdCouponDto: CreatedProductDiscountCouponDto
): RepresentationModel<CouponCreationResponse>() {
}