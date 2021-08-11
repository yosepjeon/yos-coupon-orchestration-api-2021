package com.yosep.coupon.coupon.data.jpa

import com.yosep.coupon.common.data.RandomIdGenerator
import com.yosep.coupon.coupon.data.jpa.dto.CouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import com.yosep.coupon.mapper.CouponMapper
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import javax.transaction.Transactional

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class CouponRepositoryTest @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val restTemplate: RestTemplate
) {
    val log = Slf4JLoggerFactory.getInstance(CouponRepositoryTest::class.java)!!

    @Test
    fun restTemplateTest() {
        val response = restTemplate.exchange("http://localhost:8001/products?id=test-product-category1-0",HttpMethod.GET,null,Boolean::class.java)

        log.info("" + response.body)
    }

    @Test
    @DisplayName("[Coupon Repository] 쿠폰 생성 테스트")
    fun 쿠폰_생성_테스트() {
        log.info("[Coupon Repository] 쿠폰 생성 테스트")

        val couponDtoForCreation = CouponDtoForCreation(
            "",
            "coupon1",
            CouponStockVo(100, 100),
            "test-product-category1-0",
            CouponDiscountVo(
                10000,
                0
            )
        )

        var couponId = RandomIdGenerator.generate()
        while(couponRepository.findById(couponId).isPresent) {
            couponId = RandomIdGenerator.generate()
        }

        couponDtoForCreation.couponId = couponId

        val couponForCreation = CouponMapper.INSTANCE.couponDtoForCreationToProductDiscountCoupon(couponDtoForCreation)

        val createdCoupon = couponRepository.save(couponForCreation)

        Assertions.assertEquals(createdCoupon.name, couponDtoForCreation.name)
        Assertions.assertEquals(createdCoupon.productId, couponDtoForCreation.productId)
        Assertions.assertEquals(createdCoupon.couponStock.remain, couponDtoForCreation.couponStockVo.remain)
        Assertions.assertEquals(createdCoupon.couponStock.total, couponDtoForCreation.couponStockVo.total)
        Assertions.assertEquals(createdCoupon.couponDiscount.discountAmount, couponDtoForCreation.couponDiscountVo.discountAmount)
        Assertions.assertEquals(createdCoupon.couponDiscount.discountPercent, couponDtoForCreation.couponDiscountVo.discountPercent)

        log.info("couponId: ${createdCoupon.couponId}")
        log.info("name: ${createdCoupon.name}")
        log.info("couponStock.remain: ${createdCoupon.couponStock.remain}")
        log.info("couponStock.total: ${createdCoupon.couponStock.total}")
        log.info("createdDate: ${createdCoupon.createdDate}")
        log.info("lastModifiedDate: ${createdCoupon.lastModifiedDate}")
    }
}