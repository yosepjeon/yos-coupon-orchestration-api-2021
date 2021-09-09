package com.yosep.coupon.coupon.data.jpa.repository.db

import com.yosep.coupon.coupon.data.jpa.entity.CouponEvent

interface CouponEventRepositoryQueryDsl {
    fun save(couponEvent: CouponEvent): CouponEvent
}