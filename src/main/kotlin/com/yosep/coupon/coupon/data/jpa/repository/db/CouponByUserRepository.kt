package com.yosep.coupon.coupon.data.jpa.repository.db

import com.yosep.coupon.coupon.data.jpa.entity.CouponByUser
import org.springframework.data.jpa.repository.JpaRepository

interface CouponByUserRepository: JpaRepository<CouponByUser, String>, CouponByUserRepositoryQueryDsl {
}