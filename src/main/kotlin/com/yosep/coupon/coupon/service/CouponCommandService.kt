package com.yosep.coupon.coupon.service

import com.yosep.coupon.common.exception.*
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponByUserRepository
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

@Service
class CouponCommandService @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val couponByUserRepository: CouponByUserRepository
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

        for(orderProductDiscountCouponDto in orderProductDiscountCouponDtos) {
            if(orderProductDiscountCouponDto.state != "READY") {
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