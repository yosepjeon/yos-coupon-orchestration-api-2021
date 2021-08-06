package com.yosep.coupon.coupon.service

import com.yosep.coupon.common.exception.NotExistElementException
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
     * SAGA 쿠폰 스텝 진행
     * Logic:
     */
    @Transactional(
        readOnly = false,
//        rollbackFor = [NotExistElementException::class, RuntimeException::class, NotEqualProductPrice::class, InvalidStockValueException::class],
    )
    fun processProductDiscountCouponStep(orderProductDiscountCouponStepDto: OrderProductDiscountCouponStepDto) {
        val orderCouponDtos = orderProductDiscountCouponStepDto.orderProductDiscountCouponDtos

        orderCouponDtos.forEach { orderCouponDtoForCreation ->
            orderCouponDtoForCreation.state = "PENDING"

//            val optionalCoupon = couponRepository.findById(orderCouponDtoForCreation.couponByUserId)
            val optionalCoupon = couponByUserRepository.findById(orderCouponDtoForCreation.couponByUserId)
            if(optionalCoupon.isEmpty) {
                orderCouponDtoForCreation.state = "NotExistElementException"
                throw NotExistElementException(
                    orderCouponDtoForCreation.couponByUserId + " 해당 쿠폰이 존재하지 않습니다."
                )
            }

            val selectedCoupon = optionalCoupon.get()
//            selectedCoupon.
        }
    }

    /*
     * SAGA 쿠폰 스텝 Revert
     */
    @Transactional(
        readOnly = false,
//        rollbackFor = [NotExistElementException::class, RuntimeException::class, NotEqualProductPrice::class, InvalidStockValueException::class],
    )
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    fun revertProductDiscountCouponStep(orderProductDiscountCouponStepDto: OrderProductDiscountCouponStepDto) {

    }
}