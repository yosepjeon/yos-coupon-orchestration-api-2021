package com.yosep.coupon.coupon.service

import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class CouponCommmandServiceTest @Autowired constructor(
    private val couponCommandService: CouponCommandService
) {
    private val log = Slf4JLoggerFactory.getInstance(CouponCommmandServiceTest::class.java)

    @Test
    @DisplayName("[Coupon Service] 상품할인쿠폰 생성 테스트")
    fun 상품할인쿠폰_생성_테스트() {
        log.info("[Coupon Service] Coupon 생성 테스트")

        val couponDtoForCreation = ProductDiscountCouponDtoForCreation(
            "",
            "coupon1",
            CouponStockVo(100, 100),
            "test-product-category1-0",
            CouponDiscountVo(
                10000,
                0
            )
        )

        val createdCoupon = couponCommandService.createProductDiscountCoupon(couponDtoForCreation)

        Assertions.assertEquals(createdCoupon.name, couponDtoForCreation.name)
        Assertions.assertEquals(createdCoupon.productId, couponDtoForCreation.productId)
        Assertions.assertEquals(createdCoupon.couponStockVo.remain, couponDtoForCreation.couponStockVo.remain)
        Assertions.assertEquals(createdCoupon.couponStockVo.total, couponDtoForCreation.couponStockVo.total)
        Assertions.assertEquals(createdCoupon.couponDiscountVo.discountAmount, couponDtoForCreation.couponDiscountVo.discountAmount)
        Assertions.assertEquals(createdCoupon.couponDiscountVo.discountPercent, couponDtoForCreation.couponDiscountVo.discountPercent)

        log.info("couponId: ${createdCoupon.couponId}")
        log.info("name: ${createdCoupon.name}")
        log.info("couponStock.remain: ${createdCoupon.couponStockVo.remain}")
        log.info("couponStock.total: ${createdCoupon.couponStockVo.total}")


    }

    @Test
    @DisplayName("[Coupon Service] CouponByUser 생성 테스트")
    fun CouponByUser_생성_테스트() {
        log.info("[Coupon Service] Coupon 생성 테스트")


    }
}