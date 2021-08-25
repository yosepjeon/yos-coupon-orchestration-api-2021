package com.yosep.coupon.coupon.service

import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponStepDto
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class ProductDiscountCouponCommandServiceTest @Autowired constructor(
    private val couponCommandService: CouponCommandService,
    private val productDiscountCouponCommandService: ProductDiscountCouponCommandService,
    private val totalDiscountCouponCommandService: TotalDiscountCouponCommandService
) {
    val log = Slf4JLoggerFactory.getInstance(ProductDiscountCouponCommandServiceTest::class.java)

    @Test
    @DisplayName("[Coupon Saga] 상품할인 쿠폰 SAGA 테스트")
    fun 상품할인쿠폰_SAGA_성공_테스트() {
        log.info("[Coupon Saga] 상품할인 쿠폰 SAGA 테스트")
        val orderProductDiscountCouponDtos = mutableListOf<OrderProductDiscountCouponDto>()
//        for(i in 1..5) {
//            val orderProductDiscountCouponDto = OrderProductDiscountCouponDto(
//                "PRODUCT",
//                "",
//                i,
//                "user-admin-for-test",
//                ""
//            )
//        }

//        val orderProductDiscountCouponStepDto = OrderProductDiscountCouponStepDto(
//
//        )

//        productDiscountCouponCommandService.processProductDiscountCouponStep()
    }
}