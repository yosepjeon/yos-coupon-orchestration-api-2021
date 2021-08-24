package com.yosep.coupon.coupon.controller

import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.response.TotalDiscountCouponCreationResponse
import com.yosep.coupon.coupon.service.CouponCommandService
import com.yosep.coupon.coupon.service.CouponQueryService
import com.yosep.coupon.coupon.service.TotalDiscountCouponCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import javax.validation.Valid

@RestController
@RequestMapping("/coupons/total")
class TotalDiscountCouponController @Autowired constructor(
    private val couponCommandService: CouponCommandService,
    private val totalDiscountCouponCommandService: TotalDiscountCouponCommandService,
    private val couponQueryService: CouponQueryService
) {
    @GetMapping("/test")
    fun getTotalCoupons(): ResponseEntity<*> {
        return ResponseEntity.ok("ok")
    }

    @PostMapping
    fun createTotalDiscountProductCoupon(
        @RequestBody @Valid couponDtoForCreation: TotalDiscountCouponDtoForCreation,
        errors: Errors
    ): ResponseEntity<*> {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors)
        }

        val createdCouponDto = totalDiscountCouponCommandService.createTotalDiscountCoupon(couponDtoForCreation)
        val response = TotalDiscountCouponCreationResponse(createdCouponDto)

        return ResponseEntity.ok(response)
    }

    @PostMapping("order-saga-total-coupon")
    fun processOrderToCouponUseTotalCouponSaga(
        @RequestBody orderTotalDiscountCouponStepDto: @Valid OrderProductDiscountCouponStepDto,
        errors: Errors
    ): ResponseEntity<*> {
        return if (errors.hasErrors()) {
            ResponseEntity.badRequest().body(errors)
        } else try {
            ResponseEntity.ok()
        } catch (runtimeException: RuntimeException) {
            ResponseEntity.ok()
        } as ResponseEntity<*>
    }
}