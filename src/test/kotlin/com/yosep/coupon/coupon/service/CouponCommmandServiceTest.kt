package com.yosep.coupon.coupon.service

import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class CouponCommmandServiceTest @Autowired constructor(
    private val couponCommandService: CouponCommandService
) {
    private val log = Slf4JLoggerFactory.getInstance(CouponCommmandServiceTest::class.java)

    @Test
    @DisplayName("[Coupon Service] Coupon 생성 테스트")
    fun Coupon_생성_테스트() {
        log.info("[Coupon Service] Coupon 생성 테스트")

//        val couponDtoForCreation
    }

    @Test
    @DisplayName("[Coupon Service] CouponByUser 생성 테스트")
    fun CouponByUser_생성_테스트() {
        log.info("[Coupon Service] Coupon 생성 테스트")


    }
}