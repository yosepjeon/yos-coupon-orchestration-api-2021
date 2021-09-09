package com.yosep.coupon.coupon.data.jpa.repository.db

import com.yosep.coupon.coupon.data.jpa.entity.CouponEvent
import com.yosep.coupon.coupon.data.jpa.vo.EventId
import org.springframework.data.jpa.repository.JpaRepository

interface CouponEventRepository: JpaRepository<CouponEvent, EventId> {
}