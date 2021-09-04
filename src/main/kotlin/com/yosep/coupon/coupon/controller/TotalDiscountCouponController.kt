package com.yosep.coupon.coupon.controller

import com.yosep.coupon.common.exception.AlreadyUsedException
import com.yosep.coupon.common.exception.ExpireCouponException
import com.yosep.coupon.common.exception.NotExistElementException
import com.yosep.coupon.common.exception.UsingCouponRuleViolationException
import com.yosep.coupon.coupon.data.jpa.dto.OrderTotalDiscountCouponStepDto
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

    @PostMapping("/order-saga-total-coupon")
    fun processOrderToCouponUseTotalCouponSaga(
        @RequestBody orderTotalDiscountCouponStepDto: @Valid OrderTotalDiscountCouponStepDto,
        errors: Errors
    ): ResponseEntity<*> {
        val orderTotalDiscountCouponStepDto = orderTotalDiscountCouponStepDto

        return if (errors.hasErrors()) {
            ResponseEntity.badRequest().body(errors)
        } else try {
            ResponseEntity.ok(
                totalDiscountCouponCommandService.processTotalDiscountCouponStep(
                    orderTotalDiscountCouponStepDto
                )
            )
        } catch (notExistElementException: NotExistElementException) {
            orderTotalDiscountCouponStepDto.state = notExistElementException.javaClass.simpleName
            return ResponseEntity.ok(orderTotalDiscountCouponStepDto)
        } catch (alreadyUsedException: AlreadyUsedException) {
            orderTotalDiscountCouponStepDto.state = alreadyUsedException.javaClass.simpleName
            return ResponseEntity.ok(orderTotalDiscountCouponStepDto)
        } catch (expireCouponException: ExpireCouponException) {
            orderTotalDiscountCouponStepDto.state = expireCouponException.javaClass.simpleName
            return ResponseEntity.ok(orderTotalDiscountCouponStepDto)
        } catch (usingCouponRuleViolationException: UsingCouponRuleViolationException) {
            orderTotalDiscountCouponStepDto.state = usingCouponRuleViolationException.javaClass.simpleName
            return ResponseEntity.ok(orderTotalDiscountCouponStepDto)
        } catch (runtimeException: RuntimeException) {
            orderTotalDiscountCouponStepDto.state = "EXCEPTION"
            return ResponseEntity.ok(orderTotalDiscountCouponStepDto)
        }
    }
}