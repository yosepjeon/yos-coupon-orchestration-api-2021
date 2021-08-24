package com.yosep.coupon.coupon.data.jpa.repository.db

import com.yosep.coupon.coupon.data.jpa.entity.CouponByUser
import java.util.*

interface CouponByUserRepositoryQueryDsl {
    fun findByUserId(userId: String?): Optional<CouponByUser>
}