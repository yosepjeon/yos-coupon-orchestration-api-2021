package com.yosep.coupon.coupon.data.jpa.dto.response

import com.yosep.coupon.coupon.data.jpa.dto.CreatedProductDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.CreatedTotalDiscountCouponDto
import org.springframework.hateoas.RepresentationModel

data class TotalDiscountCouponCreationResponse (
    val createdCouponDto: CreatedTotalDiscountCouponDto
): RepresentationModel<TotalDiscountCouponCreationResponse>() {
}