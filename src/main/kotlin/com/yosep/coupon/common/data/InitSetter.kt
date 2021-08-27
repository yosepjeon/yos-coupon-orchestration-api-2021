package com.yosep.coupon.common.data

import com.yosep.coupon.coupon.data.jpa.dto.CouponByUserCreationDto
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.entity.EditableState
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import com.yosep.coupon.coupon.service.CouponByUserCommandService
import com.yosep.coupon.coupon.service.CouponCommandService
import com.yosep.coupon.coupon.service.ProductDiscountCouponCommandService
import com.yosep.coupon.coupon.service.TotalDiscountCouponCommandService
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import com.yosep.coupon.mapper.CouponMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*

@Component
class InitSetter @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val couponCommandService: CouponCommandService,
    private val productDiscountCouponCommandService: ProductDiscountCouponCommandService,
    private val totalDiscountCouponCommandService: TotalDiscountCouponCommandService,
    private val couponByUserCommandService: CouponByUserCommandService
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        deleteTestData()
        setTestData()
    }

    private fun setTestData() {
        // test product 5개에 연결된 coupon
        var couponDtoForCreation: ProductDiscountCouponDtoForCreation
        // 상품 금액 할인
        for (i in 0..4) {
            couponDtoForCreation = ProductDiscountCouponDtoForCreation(
                "coupon-product-amount-test$i",
                "coupon-product-amount-test$i",
                CouponStockVo(100, 100),
                "test-product-category1-$i",
                CouponDiscountVo(
                    10000,
                    0
                )
            )

            productDiscountCouponCommandService.createProductDiscountCouponForTest(couponDtoForCreation)
        }

        // 상품 비율 할인
        for (i in 0..4) {
            couponDtoForCreation = ProductDiscountCouponDtoForCreation(
                "coupon-product-percent-test$i",
                "coupon-product-percent-test$i",
                CouponStockVo(100, 100),
                "test-product-category1-$i",
                CouponDiscountVo(
                    0,
                    10L * (i+1)
                )
            )

            productDiscountCouponCommandService.createProductDiscountCouponForTest(couponDtoForCreation)
        }

        var totalDiscountcouponDtoForCreation: TotalDiscountCouponDtoForCreation
        // 전체 금액 할인 쿠폰 3개
        for (i in 1..3) {
            totalDiscountcouponDtoForCreation = TotalDiscountCouponDtoForCreation(
                "coupon-total-amount-test$i",
                "coupon-total-amount-test$i",
                CouponStockVo(100, 100),
                CouponDiscountVo(
                    10000,
                    0
                )
            )

            totalDiscountCouponCommandService.createTotalDiscountCouponForTest(totalDiscountcouponDtoForCreation)
        }

        // 전체 퍼센트 할인 쿠폰 3개
        for (i in 1..3) {
            totalDiscountcouponDtoForCreation = TotalDiscountCouponDtoForCreation(
                "coupon-total-percent-test$i",
                "coupon-total-percent-test$i",
                CouponStockVo(100, 100),
                CouponDiscountVo(
                    0,
                    30
                )
            )

            totalDiscountCouponCommandService.createTotalDiscountCouponForTest(totalDiscountcouponDtoForCreation)
        }

        val userId = "user-admin-for-test"
        // 유저 상품 금액 할인 쿠폰 할당 3개
        for(i in 1..3) {
            val couponByUserCreationDto = CouponByUserCreationDto(
                "own-product-amount-coupon-test$i",
                "user-admin-for-test",
                "coupon-product-amount-test$i"
            )

            couponByUserCommandService.ownCouponForTest(couponByUserCreationDto)
        }

        // 유저 전체 금액 할인 쿠폰 할당 3개
        for(i in 1..3) {
            val couponByUserCreationDto = CouponByUserCreationDto(
                "own-total-amount-coupon-test$i",
                "user-admin-for-test",
                "coupon-total-amount-test$i"
            )

            couponByUserCommandService.ownCouponForTest(couponByUserCreationDto)
        }

        // 유저 상품 비율 할인 쿠폰 할당 2개
        for(i in 1..2) {
            val couponByUserCreationDto = CouponByUserCreationDto(
                "own-product-percent-coupon-test$i",
                "user-admin-for-test",
                "coupon-product-percent-test$i"
            )

            couponByUserCommandService.ownCouponForTest(couponByUserCreationDto)
        }

        // 유저 전체 비율 할인 쿠폰 할당 3개
        for(i in 1..3) {
            val couponByUserCreationDto = CouponByUserCreationDto(
                "own-total-percent-coupon-test$i",
                "user-admin-for-test",
                "coupon-total-percent-test$i"
            )

            couponByUserCommandService.ownCouponForTest(couponByUserCreationDto)
        }
    }

    private fun deleteTestData() {
        for (i in 0..4) {
            couponCommandService.deleteCouponById("coupon-product-amount-test$i")
        }

        for (i in 0..4) {
            couponCommandService.deleteCouponById("coupon-product-percent-test$i")
        }

        for (i in 1..3) {
            couponCommandService.deleteCouponById("coupon-total-amount-test$i")
        }

        for (i in 1..3) {
            couponCommandService.deleteCouponById("coupon-total-percent-test$i")
        }

        val userId = "user-admin-for-test"
        // 유저 상품 금액 할인 쿠폰 할당 3개
        for(i in 1..3) {
            couponByUserCommandService.deleteCouponByUser("own-product-amount-coupon-test$i")
        }

        // 유저 전체 비율 할인 쿠폰 할당 3개
        for(i in 1..3) {
            couponByUserCommandService.deleteCouponByUser("own-total-amount-coupon-test$i")
        }

        // 유저 상품 비율 할인 쿠폰 할당 2개
        for(i in 1..2) {
            couponByUserCommandService.deleteCouponByUser("own-product-percent-coupon-test$i")
        }

        // 유저 전체 금액 비율 쿠폰 할당 3개
        for(i in 1..3) {
            couponByUserCommandService.deleteCouponByUser("own-total-percent-coupon-test$i")
        }
    }
}