package com.yosep.coupon.coupon.service

import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CouponQueryService @Autowired constructor(
    var couponRepository: CouponRepository
) {
}