package com.yosep.coupon.coupon.service

import com.yosep.coupon.coupon.data.jpa.dto.CouponByUserCreationDto
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class CouponByUserCommandServiceTest @Autowired constructor(
    private val couponCommandService: CouponCommandService,
    private val productDiscountCouponCommandService: ProductDiscountCouponCommandService,
    private val totalDiscountCouponCommandService: TotalDiscountCouponCommandService,
    private val couponByUserCommandService: CouponByUserCommandService,
    private val restTemplate: RestTemplate
) {
    private val log = Slf4JLoggerFactory.getInstance(CouponByUserCommandServiceTest::class.java)

    var couponId1: String = ""
    var couponId2: String = ""
    var couponId3: String = ""

    @BeforeEach
    fun setCoupons() {
        val couponDtoForCreation1 = ProductDiscountCouponDtoForCreation(
            "",
            "coupon1",
            CouponStockVo(100, 100),
            "test-product-category1-0",
            CouponDiscountVo(
                10000,
                0
            )
        )

        val couponDtoForCreation2 = ProductDiscountCouponDtoForCreation(
            "",
            "coupon2",
            CouponStockVo(100, 100),
            "test-product-category1-1",
            CouponDiscountVo(
                10000,
                0
            )
        )

        val couponDtoForCreation3 = TotalDiscountCouponDtoForCreation(
            "",
            "coupon3",
            CouponStockVo(100, 100),
            CouponDiscountVo(
                10000,
                0
            )
        )

        val createdCoupon1 = productDiscountCouponCommandService.createProductDiscountCoupon(couponDtoForCreation1)
        val createdCoupon2 = productDiscountCouponCommandService.createProductDiscountCoupon(couponDtoForCreation2)
        val createdCoupon3 = totalDiscountCouponCommandService.createTotalDiscountCoupon(couponDtoForCreation3)
        couponId1 = createdCoupon1.couponId
        couponId2 = createdCoupon2.couponId
        couponId3 = createdCoupon3.couponId
    }

    @AfterEach
    fun deleteCoupons() {
        couponCommandService.deleteCouponById(couponId1)
        couponCommandService.deleteCouponById(couponId2)
        couponCommandService.deleteCouponById(couponId3)
    }

    @Test
    @DisplayName("[CouponByUserCommandService] 유저가 쿠폰 한개 소유하기 테스트")
    fun 유저가_쿠폰_한개_소유하기_테스트() {
        val couponByUserCreationDto = CouponByUserCreationDto(
            "",
            "user-admin-for-test",
            couponId1
        )

        val createdCouponByUser = couponByUserCommandService.ownCoupon(couponByUserCreationDto)

        Assertions.assertEquals(couponByUserCreationDto.couponId,createdCouponByUser.coupon.couponId)
        Assertions.assertEquals(couponByUserCreationDto.userId, createdCouponByUser.userId)
        log.info("id: ${createdCouponByUser.id}")
        log.info("userId: ${createdCouponByUser.userId}")
        log.info("coupon: ${createdCouponByUser.coupon.couponId}")
        // TODO: Test Context에서는 조회가 안되는 이유 찾아보기
        log.info("coupon type: ${createdCouponByUser.coupon.dtype}")
        log.info("state: ${createdCouponByUser.state}")
    }

    @Test
    fun httpConnectionToUserApiTest() {
        val response = restTemplate.exchange(
            "http://localhost:8001/users?userId=enekelx1",
            HttpMethod.GET, null, Boolean::class.java
        )

        log.info(response.toString())

        val response2 = restTemplate.exchange(
            "http://localhost:8001/products?id=create-product-category1-0",
            HttpMethod.GET, null, Boolean::class.java
        )

        log.info( "${response2.body}")
    }
}