package com.yosep.coupon.coupon.controller

import com.yosep.coupon.coupon.service.CouponCommandService
import com.yosep.coupon.coupon.service.CouponQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import javax.validation.Valid

@RestController
@RequestMapping("/coupons")
class CouponController @Autowired constructor(
    private val couponCommandService: CouponCommandService,
    private val couponQueryService: CouponQueryService,
    private val couponByUserCommandService: CouponCommandService
) {
    @GetMapping("/test")
    fun test():ResponseEntity<*> {
        return ResponseEntity.ok("")
    }

    @DeleteMapping
    fun deleteCoupon(@RequestParam(value = "id") couponId: String): ResponseEntity<*> {
        val result = couponCommandService.deleteCouponById(couponId)

        if(result) {
            return ResponseEntity.ok(result)
        }else {
            return ResponseEntity.notFound().build<Boolean>()
        }
    }
}