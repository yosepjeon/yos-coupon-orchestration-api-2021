package com.yosep.coupon.coupon.service

import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CouponQueryService @Autowired constructor(
    private val couponRepository: CouponRepository
) {
    val log = Slf4JLoggerFactory.getInstance(CouponQueryService::class.java)

    /*
    * 특정 쿠폰 조회
    * Logic:
     */
    fun findCouponById(couponId: String) {
        val optionalCoupon = couponRepository.findById(couponId)

        if(optionalCoupon.isEmpty) {
            // No Exist Exception
        }

        val selectedCoupon = optionalCoupon.get()
    }

    /*
    * 전체 쿠폰 조회
     */
    fun findAllCoupons() {

    }

    /*
    * 회사 별 쿠폰 조회
     */
    fun findCouponsByCompany() {

    }

}