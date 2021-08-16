package com.yosep.coupon.coupon.data.jpa.dto.response

import com.yosep.coupon.coupon.data.jpa.dto.CreatedProductDiscountCouponDto
import org.springframework.hateoas.RepresentationModel

data class ProductDiscountCouponCreationResponse(
    val createdCouponDto: CreatedProductDiscountCouponDto
): RepresentationModel<ProductDiscountCouponCreationResponse>() {
}