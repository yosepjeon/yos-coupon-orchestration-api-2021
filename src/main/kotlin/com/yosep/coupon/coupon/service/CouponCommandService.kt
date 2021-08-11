package com.yosep.coupon.coupon.service

import com.yosep.coupon.common.data.RandomIdGenerator
import com.yosep.coupon.common.exception.*
import com.yosep.coupon.coupon.data.jpa.dto.CouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponByUserRepository
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import com.yosep.coupon.mapper.CouponMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Lock
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import javax.persistence.LockModeType

@Service
class CouponCommandService @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val couponByUserRepository: CouponByUserRepository,
    private val restTemplate: RestTemplate
) {

    /*
    * 쿠폰 생성
    * Logic:
    *
     */
    fun createCoupon(couponDtoForCreation: CouponDtoForCreation) {
        if(checkIsPresentProduct(couponDtoForCreation.productId) != true) {

        }

        var couponId = RandomIdGenerator.generate()
        while(couponRepository.findById(couponId).isPresent) {
            couponId = RandomIdGenerator.generate()
        }

        couponDtoForCreation.couponId = couponId

        val couponForCreation = CouponMapper.INSTANCE.couponDtoForCreationToProductDiscountCoupon(couponDtoForCreation)
    }

    private fun checkIsPresentProduct(productId: String):Boolean? {
        val response = restTemplate.exchange("http://localhost:8001/products?id=test-product-category1-0",
            HttpMethod.GET,null,Boolean::class.java)

        return response.body
    }

    /*
     * SAGA 상품 할인 쿠폰 스텝 진행
     * Logic:
     */
    @Transactional(
        readOnly = false,
        rollbackFor = [NotExistElementException::class, RuntimeException::class, NotEqualDiscountAmountException::class, NotEqualDiscountPercentException::class, InvalidPriceException::class, NoHasCouponException::class],
    )
    fun processProductDiscountCouponStep(orderProductDiscountCouponStepDto: OrderProductDiscountCouponStepDto): OrderProductDiscountCouponStepDto {
        val orderCouponDtoForCreations = orderProductDiscountCouponStepDto.orderProductDiscountCouponDtos

        orderCouponDtoForCreations.forEach { orderCouponDtoForCreation ->
            orderCouponDtoForCreation.state = "PENDING"

            val optionalCoupon = couponByUserRepository.findById(orderCouponDtoForCreation.couponByUserId)
            if (optionalCoupon.isEmpty) {
                orderCouponDtoForCreation.state = "NotExistElementException"
                throw NotExistElementException(
                    orderCouponDtoForCreation.couponByUserId + " 해당 쿠폰이 존재하지 않습니다."
                )
            }

            val selectedCoupon = optionalCoupon.get()
            selectedCoupon.use(orderCouponDtoForCreation)
            orderCouponDtoForCreation.state = "COMP"
        }

        return orderProductDiscountCouponStepDto
    }

    fun validateSagaCouponDtos(orderProductDiscountCouponStepDto: OrderProductDiscountCouponStepDto) {
        val orderProductDiscountCouponDtos = orderProductDiscountCouponStepDto.orderProductDiscountCouponDtos

        for (orderProductDiscountCouponDto in orderProductDiscountCouponDtos) {
            if (orderProductDiscountCouponDto.state != "READY") {
                continue
            }

            val optionalCoupon = couponByUserRepository.findById(orderProductDiscountCouponDto.couponByUserId)
            if (optionalCoupon.isEmpty) {
                orderProductDiscountCouponDto.state = "NotExistElementException"
                continue
            }

            val selectedCoupon = optionalCoupon.get()

            selectedCoupon.validateCouponDtoNotPublishException(orderProductDiscountCouponDto)
        }
    }

    /*
     * SAGA 상품 할인 쿠폰 스텝 Revert
     */
    @Transactional(
        readOnly = false,
//        rollbackFor = [NotExistElementException::class, RuntimeException::class, NotEqualProductPrice::class, InvalidStockValueException::class],
    )
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    fun revertProductDiscountCouponStep(orderProductDiscountCouponStepDto: OrderProductDiscountCouponStepDto) {

    }

    /*
     * SAGA 상품 할인 쿠폰 스텝 진행
     * Logic:
     */


    /*
     * SAGA 상품 할인 쿠폰 스텝 Revert
     */
}