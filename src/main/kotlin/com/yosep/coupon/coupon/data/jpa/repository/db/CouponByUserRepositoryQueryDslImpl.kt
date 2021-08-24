package com.yosep.coupon.coupon.data.jpa.repository.db

import com.querydsl.jpa.impl.JPAQueryFactory
import com.yosep.coupon.coupon.data.jpa.entity.CouponByUser
import com.yosep.coupon.coupon.data.jpa.entity.QCouponByUser
import com.yosep.coupon.coupon.data.jpa.entity.QCouponByUser.couponByUser
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class CouponByUserRepositoryQueryDslImpl @Autowired constructor(
    private val jpaQueryFactory: JPAQueryFactory
): CouponByUserRepositoryQueryDsl {
    override fun findByUserId(userId: String?): Optional<CouponByUser> {
        val couponByUser = jpaQueryFactory.selectFrom(couponByUser)
            .where(QCouponByUser.couponByUser.userId.eq(userId))
            .fetchOne()


        return if(couponByUser != null) {
            Optional.of(couponByUser)
        }else {
            Optional.empty()
        }
    }

}