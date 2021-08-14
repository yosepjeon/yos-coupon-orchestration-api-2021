package com.yosep.coupon.mapper

import com.yosep.coupon.common.data.RandomIdGenerator
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class MapperTest @Autowired constructor(
    private val couponRepository: CouponRepository
) {
    val log = Slf4JLoggerFactory.getInstance(MapperTest::class.java)

    @Test
    @DisplayName("[Coupon Mapper Test] CouponDtoForCreation에서 Coupon 변환 테스트")
    fun MappingCouponDtoForCreationToCouponTest() {
        log.info("[Coupon Mapper Test] CouponDtoForCreation에서 Coupon 변환 테스트")

        val couponDtoForCreation = ProductDiscountCouponDtoForCreation(
            "",
            "coupon1",
            CouponStockVo(100, 100),
            "product1",
            CouponDiscountVo(
                10000,
                0
            )
        )

        val coupon = CouponMapper.INSTANCE.dtoToEntity(couponDtoForCreation)

        Assertions.assertEquals(coupon.name, couponDtoForCreation.name)
        Assertions.assertEquals(coupon.productId, couponDtoForCreation.productId)
        Assertions.assertEquals(coupon.couponStock.remain, couponDtoForCreation.couponStockVo.remain)
        Assertions.assertEquals(coupon.couponStock.total, couponDtoForCreation.couponStockVo.total)
        Assertions.assertEquals(coupon.couponDiscount.discountAmount, couponDtoForCreation.couponDiscountVo.discountAmount)
        Assertions.assertEquals(coupon.couponDiscount.discountPercent, couponDtoForCreation.couponDiscountVo.discountPercent)
    }

    @Test
    @DisplayName("[Coupon Mapper Test] Coupon에서 CreatedCouponDto 변환 테스트")
    fun MappingCouponToCreatedCouponDtoTest() {
        log.info("[Coupon Mapper Test] Coupon에서 CreatedCouponDto 변환 테스트")

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
        while(couponRepository.findById(couponId).isPresent) {
            couponId = RandomIdGenerator.generate()
        }

        couponDtoForCreation.couponId = couponId

        val couponForCreation = CouponMapper.INSTANCE.dtoToEntity(couponDtoForCreation)

        val createdCoupon = couponRepository.save(couponForCreation)

        val createdProductDiscountCouponDto = CouponMapper.INSTANCE.entityToDto(createdCoupon)

        log.info(createdProductDiscountCouponDto.toString())
    }
}