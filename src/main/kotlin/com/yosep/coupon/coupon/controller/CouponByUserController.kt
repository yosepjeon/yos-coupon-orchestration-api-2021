package com.yosep.coupon.coupon.controller

import com.yosep.coupon.common.exception.NotExistUserException
import com.yosep.coupon.coupon.data.jpa.dto.CouponByUserCreationDto
import com.yosep.coupon.coupon.service.CouponByUserCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/coupon-by-users")
class CouponByUserController @Autowired constructor(
    private val couponByUserCommandService: CouponByUserCommandService
) {
    @PostMapping
    fun ownCoupon(
        couponByUserCreationDto: @Valid CouponByUserCreationDto,
        errors: Errors
    ): ResponseEntity<*> {
        return if (errors.hasErrors()) {
            ResponseEntity.badRequest().body(errors)
        } else try {
            ResponseEntity.ok(true)
        } catch (notExistUserException: NotExistUserException) {
            ResponseEntity.ok("")
        }
    }
}