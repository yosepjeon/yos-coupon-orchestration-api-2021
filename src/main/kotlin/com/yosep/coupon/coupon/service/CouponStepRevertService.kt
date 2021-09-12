package com.yosep.coupon.coupon.service

import com.yosep.coupon.common.exception.*
import com.yosep.coupon.coupon.data.event.RevertProductDiscountCouponStepEvent
import com.yosep.coupon.coupon.data.event.RevertTotalDiscountCouponStepEvent
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.dto.OrderTotalDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.entity.CouponEvent
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponByUserRepository
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponEventRepository
import com.yosep.coupon.coupon.data.jpa.vo.EventId
import com.yosep.coupon.coupon.data.jpa.vo.EventType
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Service
class CouponStepRevertService @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val couponByUserRepository: CouponByUserRepository,
    private val couponEventRepository: CouponEventRepository,
    private val restTemplate: RestTemplate
) {
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
    @Transactional(readOnly = false)
    fun revertProductDiscountCouponStep(revertProductDiscountCouponStepEvent: RevertProductDiscountCouponStepEvent) {
        val couponEvent = CouponEvent(
            EventId(
                revertProductDiscountCouponStepEvent.eventId,
                EventType.REVERT_PRODUCT_DISCOUNT_COUPON
            )
        )

        couponEventRepository.save(couponEvent)

        val orderProductDiscountDtos = revertProductDiscountCouponStepEvent.orderProductDiscountCouponDtos

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
    }

    /*
     * SAGA 전체 할인 쿠폰 스텝 Revert
     * Logic:
     */
    @Transactional(readOnly = false)
    fun revertTotalDiscountCouponStep(revertTotalDiscountCouponStepEvent: RevertTotalDiscountCouponStepEvent) {
        val couponEvent = CouponEvent(
            EventId(
                revertTotalDiscountCouponStepEvent.eventId,
                EventType.REVERT_TOTAL_DISCOUNT_COUPON
            )
        )

        couponEventRepository.save(couponEvent)

        val orderTotalDiscountDtos = revertTotalDiscountCouponStepEvent.orderTotalDiscountCouponDtos

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
    }
}