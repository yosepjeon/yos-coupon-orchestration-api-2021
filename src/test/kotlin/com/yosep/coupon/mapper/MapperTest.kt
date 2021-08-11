package com.yosep.coupon.mapper

import com.yosep.coupon.coupon.data.jpa.dto.CouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.*

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class MapperTest {
    val log = Slf4JLoggerFactory.getInstance(MapperTest::class.java)

    @Test
    @DisplayName("[Coupon Mapper Test] CouponDtoForCreation에서 Coupon 변환 테스트")
    fun MappingCouponDtoForCreationToCouponTest() {
        log.info("[Coupon Mapper Test] CouponDtoForCreation에서 Coupon 변환 테스트")

        val couponDtoForCreation = CouponDtoForCreation(
            "",
            "coupon1",
            CouponStockVo(100, 100),
            "product1",
            CouponDiscountVo(
                10000,
                0
            )
        )

        val coupon = CouponMapper.INSTANCE.couponDtoForCreationToProductDiscountCoupon(couponDtoForCreation)

        Assertions.assertEquals(coupon.name, couponDtoForCreation.name)
        Assertions.assertEquals(coupon.productId, couponDtoForCreation.productId)
        Assertions.assertEquals(coupon.couponStock.remain, couponDtoForCreation.couponStockVo.remain)
        Assertions.assertEquals(coupon.couponStock.total, couponDtoForCreation.couponStockVo.total)
        Assertions.assertEquals(coupon.couponDiscount.discountAmount, couponDtoForCreation.couponDiscountVo.discountAmount)
        Assertions.assertEquals(coupon.couponDiscount.discountPercent, couponDtoForCreation.couponDiscountVo.discountPercent)
    }
}