package com.yosep.coupon.coupon.controller

import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.response.ProductDiscountCouponCreationResponse
import com.yosep.coupon.coupon.service.CouponCommandService
import com.yosep.coupon.coupon.service.CouponQueryService
import com.yosep.coupon.coupon.service.ProductDiscountCouponCommandService
import com.yosep.coupon.coupon.service.TotalDiscountCouponCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.RuntimeException
import javax.validation.Valid

@RestController
@RequestMapping("/coupons/product")
class ProductDiscountCouponController @Autowired constructor(
    private val couponCommandService: CouponCommandService,
    private val productDiscountCouponCommandService: ProductDiscountCouponCommandService,
    private val couponQueryService: CouponQueryService
) {
    @PostMapping
    fun createProductDiscountCoupon(
        @RequestBody @Valid couponDtoForCreation: ProductDiscountCouponDtoForCreation,
        errors: Errors
    ): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors)
        }

        try {
            val createdCouponDto = productDiscountCouponCommandService.createProductDiscountCoupon(couponDtoForCreation)
            val response = ProductDiscountCouponCreationResponse(createdCouponDto)

            return ResponseEntity.ok(response)
        }catch (rex: RuntimeException) {
            return ResponseEntity.ok(rex)
        }
//        response.add(linkTo(methodOn(CouponController::class.java).createProductDiscountCoupon(couponDtoForCreation,errors)).withRel("insert-coupon"))
//        response.add(Link.of(methodOn(CouponController::class.java),"insert-coupon"))

    }

    @PostMapping("/order-saga-product-coupon")
    fun processOrderToCouponUseProductCouponSaga(
        @RequestBody orderProductDiscountCouponStepDto: @Valid OrderProductDiscountCouponStepDto,
        errors: Errors
    ): ResponseEntity<*> {
        val orderProductDiscountCouponStepDto = orderProductDiscountCouponStepDto
        return if (errors.hasErrors()) {
            ResponseEntity.badRequest().body(errors)
        } else try {
//            productDiscountCouponCommandService

            ResponseEntity.ok()
        } catch (runtimeException: RuntimeException) {
            ResponseEntity.ok()
        } as ResponseEntity<*>
    }
}