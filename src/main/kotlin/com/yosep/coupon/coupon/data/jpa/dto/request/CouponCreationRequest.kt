package com.yosep.coupon.coupon.data.jpa.dto.request

import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
//import org.springframework.hateoas.RepresentationModel
import javax.validation.constraints.NotNull

data class CouponCreationRequest(
    @field:NotNull
    val couponDtoForCreation: ProductDiscountCouponDtoForCreation
)