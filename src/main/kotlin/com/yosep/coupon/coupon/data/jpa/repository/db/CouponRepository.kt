package com.yosep.coupon.data.jpa.repository.db

import com.yosep.coupon.coupon.data.jpa.entity.Coupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CouponRepository: JpaRepository<Coupon, String> {
}