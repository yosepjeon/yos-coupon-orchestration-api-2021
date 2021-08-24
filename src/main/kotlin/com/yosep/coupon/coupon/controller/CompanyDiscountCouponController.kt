package com.yosep.coupon.coupon.controller

import com.yosep.coupon.coupon.service.CouponCommandService
import com.yosep.coupon.coupon.service.CouponQueryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coupons.company")
class CompanyDiscountCouponController@Autowired constructor(
    private val couponCommandService: CouponCommandService,
    private val couponQueryService: CouponQueryService
) {
    @PostMapping
    fun createCompanyDiscountCoupon() {

    }

    @PostMapping("order-saga-company-coupon")
    fun processOrderToCouponUseCompanyCouponSaga() {

    }
}