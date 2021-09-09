package com.yosep.coupon.coupon.data.jpa.repository.db

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yosep.coupon.coupon.data.jpa.entity.CouponEvent
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class CouponEventRepositoryQueryDslImpl @Autowired constructor(
    private val jpaQueryFactory: JPAQueryFactory
): CouponEventRepositoryQueryDsl {
    override fun save(couponEvent: CouponEvent): CouponEvent {
        TODO("Not yet implemented")
    }


}