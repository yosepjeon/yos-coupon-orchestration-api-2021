package com.yosep.coupon.coupon.service

import com.yosep.coupon.common.exception.*
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.dto.OrderTotalDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponByUserRepository
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Service
@Transactional(readOnly = false)
class CouponStepService @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val couponByUserRepository: CouponByUserRepository,
    private val restTemplate: RestTemplate
) {
    /*
     * SAGA 상품 할인 쿠폰 스텝 진행
     * Logic:
     */
    @Transactional(
        readOnly = false,
        rollbackFor = [NotExistElementException::class, RuntimeException::class, NotEqualDiscountAmountException::class, NotEqualDiscountPercentException::class, InvalidPriceException::class, NoHasCouponException::class],
    )
    fun processProductDiscountCouponStep(orderProductDiscountCouponStepDto: OrderProductDiscountCouponStepDto): OrderProductDiscountCouponStepDto {
        val orderProductDiscountDtos = orderProductDiscountCouponStepDto.orderProductDiscountCouponDtos
        orderProductDiscountCouponStepDto.state = "PENDING"

        orderProductDiscountDtos.forEach { orderProductDiscountCouponDto ->
            orderProductDiscountCouponDto.state = "PENDING"

            val optionalCoupon = couponByUserRepository.findById(orderProductDiscountCouponDto.couponByUserId)
            if (optionalCoupon.isEmpty) {
                orderProductDiscountCouponStepDto.state = "EXCEPTION"
                orderProductDiscountCouponDto.state = "NotExistElementException"
                throw NotExistElementException(
                    orderProductDiscountCouponDto.couponByUserId + " 해당 쿠폰이 존재하지 않습니다."
                )
            }

            val selectedCoupon = optionalCoupon.get()
            selectedCoupon.use(orderProductDiscountCouponDto)
            orderProductDiscountCouponDto.state = "COMP"
        }

        if (orderProductDiscountCouponStepDto.state == "PENDING") {
            orderProductDiscountCouponStepDto.state = "COMP"
        }

        return orderProductDiscountCouponStepDto
    }

    fun validateSagaProductDiscountCouponDtos(orderProductDiscountCouponStepDto: OrderProductDiscountCouponStepDto) {
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
    fun revertProductDiscountCouponStep(orderProductDiscountCouponStepDto: OrderProductDiscountCouponStepDto): OrderProductDiscountCouponStepDto {
        val orderProductDiscountDtos = orderProductDiscountCouponStepDto.orderProductDiscountCouponDtos
        orderProductDiscountCouponStepDto.state = "PENDING"

        orderProductDiscountDtos.forEach { orderProductDiscountCouponDto ->
            orderProductDiscountCouponDto.state = "REVERT-PENDING"

            val optionalCoupon = couponByUserRepository.findById(orderProductDiscountCouponDto.couponByUserId)
            if (optionalCoupon.isEmpty) {
                orderProductDiscountCouponDto.state = "NotExistElementException"
            } else {
                val selectedCoupon = optionalCoupon.get()
                selectedCoupon.revert()
                orderProductDiscountCouponDto.state = "REVERTED"
            }
        }

        orderProductDiscountCouponStepDto.state = "COMP"
        return orderProductDiscountCouponStepDto
    }

    /*
     * SAGA 전체 할인 쿠폰 스텝 진행
     * Logic:
     */
    @Transactional(
        readOnly = false,
//        rollbackFor = [NotExistElementException::class, RuntimeException::class, NotEqualProductPrice::class, InvalidStockValueException::class],
    )
    fun processTotalDiscountCouponStep(orderTotalDiscountCouponStepDto: OrderTotalDiscountCouponStepDto): OrderTotalDiscountCouponStepDto {
        val orderTotalDiscountDtos = orderTotalDiscountCouponStepDto.orderTotalDiscountCouponDtos
        orderTotalDiscountCouponStepDto.state = "PENDING"

        orderTotalDiscountDtos.forEach { orderTotakDiscountCouponDto ->
            orderTotakDiscountCouponDto.state = "PENDING"

            val optionalCoupon = couponByUserRepository.findById(orderTotakDiscountCouponDto.couponByUserId)
            if (optionalCoupon.isEmpty) {
                orderTotakDiscountCouponDto.state = "NotExistElementException"
                orderTotalDiscountCouponStepDto.state = "EXCEPTION"
                throw NotExistElementException(
                    orderTotakDiscountCouponDto.couponByUserId + " 해당 쿠폰이 존재하지 않습니다."
                )
            }

            val selectedCoupon = optionalCoupon.get()
            selectedCoupon.use(orderTotakDiscountCouponDto)
            orderTotakDiscountCouponDto.state = "COMP"
        }

        if (orderTotalDiscountCouponStepDto.state == "PENDING") {
            orderTotalDiscountCouponStepDto.state = "COMP"
        }

        return orderTotalDiscountCouponStepDto
    }


    /*
     * SAGA 전체 할인 쿠폰 스텝 Revert
     * Logic:
     */
    fun revertTotalDiscountCouponStep(orderTotalDiscountCouponStepDto: OrderTotalDiscountCouponStepDto): OrderTotalDiscountCouponStepDto {
        val orderTotalDiscountDtos = orderTotalDiscountCouponStepDto.orderTotalDiscountCouponDtos

        orderTotalDiscountDtos.forEach { orderTotalDiscountDto ->
            orderTotalDiscountDto.state = "REVERT-PENDING"

            val optionalCoupon = couponByUserRepository.findById(orderTotalDiscountDto.couponByUserId)
            if (optionalCoupon.isEmpty) {
                orderTotalDiscountDto.state = "NotExistElementException"
            } else {
                val selectedCoupon = optionalCoupon.get()
                selectedCoupon.revert()
                orderTotalDiscountDto.state = "REVERTED"
            }
        }

        return orderTotalDiscountCouponStepDto
    }
}