package com.yosep.coupon.coupon.service

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
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class CouponQueryServiceTest @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val couponQueryService: CouponQueryService
){
    private val log = Slf4JLoggerFactory.getInstance(CouponQueryServiceTest::class.java)

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
        couponForCreation.editableState = EditableState.ON

        val createdCoupon3 = couponRepository.save(totalDiscountcouponForCreation)
    }

    @Test
    @DisplayName("[Coupon Query Service]")
    fun 쿠폰_조회_테스트() {

    }
}