package com.yosep.coupon.coupon.controller

import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.response.ProductDiscountCouponCreationResponse
import com.yosep.coupon.coupon.data.jpa.dto.response.TotalDiscountCouponCreationResponse
import com.yosep.coupon.coupon.service.CouponCommandService
import com.yosep.coupon.coupon.service.CouponQueryService
import com.yosep.coupon.mapper.CouponMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/coupons")
class CouponController @Autowired constructor(
    private val couponCommandService: CouponCommandService,
    private val couponQueryService: CouponQueryService
) {


    @PostMapping("/product")
    fun createProductDiscountCoupon(@RequestBody @Valid couponDtoForCreation: ProductDiscountCouponDtoForCreation, errors:Errors): ResponseEntity<*> {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors)
        }

        val createdCouponDto = couponCommandService.createProductDiscountCoupon(couponDtoForCreation)
        val response = ProductDiscountCouponCreationResponse(createdCouponDto)
//        response.add(linkTo(methodOn(CouponController::class.java).createProductDiscountCoupon(couponDtoForCreation,errors)).withRel("insert-coupon"))
//        response.add(Link.of(methodOn(CouponController::class.java),"insert-coupon"))

        return ResponseEntity.ok(response)
    }

    @PostMapping("/total")
    fun createTotalDiscountProductCoupon(@RequestBody @Valid couponDtoForCreation: TotalDiscountCouponDtoForCreation, errors:Errors): ResponseEntity<*> {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors)
        }

        val createdCouponDto = couponCommandService.createTotalDiscountCoupon(couponDtoForCreation)
        val response = TotalDiscountCouponCreationResponse(createdCouponDto)

        return ResponseEntity.ok(response)
    }

    @PostMapping("/company")
    fun createCompanyDiscountCoupon() {

    }
}