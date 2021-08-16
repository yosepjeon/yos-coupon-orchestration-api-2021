package com.yosep.coupon.coupon.data.jpa

import com.yosep.coupon.common.data.RandomIdGenerator
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.entity.EditableState
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import com.yosep.coupon.mapper.CouponMapper
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
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

    @BeforeEach
    fun setData() {
        var couponDtoForCreation = ProductDiscountCouponDtoForCreation(
            "coupon-repository-test1",
            "coupon1",
            CouponStockVo(100, 100),
            "test-product-category1-0",
            CouponDiscountVo(
                10000,
                0
            )
        )

        var couponForCreation = CouponMapper.INSTANCE.dtoToEntity(couponDtoForCreation)
        couponForCreation.editableState = EditableState.ON

        val createdCoupon1 = couponRepository.save(couponForCreation)

        couponDtoForCreation = ProductDiscountCouponDtoForCreation(
            "coupon-repository-test2",
            "coupon2",
            CouponStockVo(100, 100),
            "test-product-category1-1",
            CouponDiscountVo(
                10000,
                0
            )
        )

        couponForCreation = CouponMapper.INSTANCE.dtoToEntity(couponDtoForCreation)
        couponForCreation.editableState = EditableState.ON

        val createdCoupon2 = couponRepository.save(couponForCreation)

        var totalDiscountcouponDtoForCreation = TotalDiscountCouponDtoForCreation(
            "coupon-repository-test3",
            "coupon3",
            CouponStockVo(100, 100),
            CouponDiscountVo(
                10000,
                0
            )
        )

        var totalDiscountcouponForCreation = CouponMapper.INSTANCE.dtoToEntity(totalDiscountcouponDtoForCreation)
        totalDiscountcouponForCreation.editableState = EditableState.ON

        val createdCoupon3 = couponRepository.save(totalDiscountcouponForCreation)
    }

    @Test
    fun restTemplateTest() {
        val response = restTemplate.exchange(
            "http://localhost:8001/products?id=test-product-category1-0",
            HttpMethod.GET,
            null,
            Boolean::class.java
        )

        log.info("" + response.body)
    }

    @Test
    @DisplayName("[Coupon Repository] 상품할인쿠폰 생성 테스트")
    fun 상품할인쿠폰_생성_테스트() {
        log.info("[Coupon Repository] 상품할인쿠폰 생성 테스트")

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

        var couponId = RandomIdGenerator.generate()
        while (couponRepository.findById(couponId).isPresent) {
            couponId = RandomIdGenerator.generate()
        }

        couponDtoForCreation.couponId = couponId

        val couponForCreation = CouponMapper.INSTANCE.dtoToEntity(couponDtoForCreation)
        couponForCreation.editableState = EditableState.ON

        val createdCoupon = couponRepository.save(couponForCreation)

        Assertions.assertEquals(createdCoupon.name, couponDtoForCreation.name)
        Assertions.assertEquals(createdCoupon.productId, couponDtoForCreation.productId)
        Assertions.assertEquals(createdCoupon.couponStock.remain, couponDtoForCreation.couponStockVo.remain)
        Assertions.assertEquals(createdCoupon.couponStock.total, couponDtoForCreation.couponStockVo.total)
        Assertions.assertEquals(
            createdCoupon.couponDiscount.discountAmount,
            couponDtoForCreation.couponDiscountVo.discountAmount
        )
        Assertions.assertEquals(
            createdCoupon.couponDiscount.discountPercent,
            couponDtoForCreation.couponDiscountVo.discountPercent
        )

        log.info("couponId: ${createdCoupon.couponId}")
        log.info("name: ${createdCoupon.name}")
        log.info("couponStock.remain: ${createdCoupon.couponStock.remain}")
        log.info("couponStock.total: ${createdCoupon.couponStock.total}")
        log.info("createdDate: ${createdCoupon.createdDate}")
        log.info("lastModifiedDate: ${createdCoupon.lastModifiedDate}")
    }

    @Test
    @DisplayName("[Coupon Repository] 쿠폰 조회 테스트")
    fun 쿠폰_조회_테스트() {
        val couponId1 = "coupon-product-amount-test1"
        val couponId2 = "coupon-product-percent-test1"
        val couponId3 = "coupon-total-amount-test1"
        val couponId4 = "coupon-total-percent-test1"

        val selectedCoupon1 = couponRepository.findById(couponId1).get()
        val selectedCoupon2 = couponRepository.findById(couponId2).get()
        val selectedCoupon3 = couponRepository.findById(couponId3).get()
        val selectedCoupon4 = couponRepository.findById(couponId4).get()

        log.info("couponId: ${selectedCoupon1.couponId} couponName: ${selectedCoupon1.name} dtype: ${selectedCoupon1.dtype}")
        log.info("couponId: ${selectedCoupon2.couponId} couponName: ${selectedCoupon2.name} dtype: ${selectedCoupon2.dtype}")
        log.info("couponId: ${selectedCoupon3.couponId} couponName: ${selectedCoupon3.name} dtype: ${selectedCoupon3.dtype}")
        log.info("couponId: ${selectedCoupon4.couponId} couponName: ${selectedCoupon4.name} dtype: ${selectedCoupon4.dtype}")
    }
}