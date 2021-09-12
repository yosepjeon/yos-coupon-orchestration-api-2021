package com.yosep.coupon.coupon.service

import com.yosep.coupon.common.data.RandomIdGenerator
import com.yosep.coupon.common.exception.NotExistElementException
import com.yosep.coupon.coupon.data.jpa.dto.CreatedTotalDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.OrderTotalDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.entity.CouponEvent
import com.yosep.coupon.coupon.data.jpa.vo.CouponState
import com.yosep.coupon.coupon.data.jpa.entity.EditableState
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponByUserRepository
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponEventRepository
import com.yosep.coupon.coupon.data.jpa.vo.EventId
import com.yosep.coupon.coupon.data.jpa.vo.EventType
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import com.yosep.coupon.mapper.CouponMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Service
@Transactional(readOnly = false)
class TotalDiscountCouponCommandService @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val couponByUserRepository: CouponByUserRepository,
    private val couponEventRepository: CouponEventRepository,
    private val restTemplate: RestTemplate
) {
    /*
    * 전체 할인 쿠폰 생성
    * Logic:
    *
     */
    fun createTotalDiscountCoupon(totalDiscountCouponDtoForCreation: TotalDiscountCouponDtoForCreation): CreatedTotalDiscountCouponDto {
        var couponId = RandomIdGenerator.generate()
        while (couponRepository.findById(couponId).isPresent) {
            couponId = RandomIdGenerator.generate()
        }

        totalDiscountCouponDtoForCreation.couponId = couponId

        val couponForCreation = CouponMapper.INSTANCE.dtoToEntity(totalDiscountCouponDtoForCreation)
        couponForCreation.editableState = EditableState.ON

        val createdCoupon = couponRepository.save(couponForCreation)

        return CouponMapper.INSTANCE.entityToDto(createdCoupon)
    }

    fun createTotalDiscountCouponForTest(couponDtoForCreation: TotalDiscountCouponDtoForCreation) {
        val couponForCreation = CouponMapper.INSTANCE.dtoToEntity(couponDtoForCreation)
        couponForCreation.editableState = EditableState.ON
        couponForCreation.state = CouponState.READY
        couponRepository.save(couponForCreation)
    }

    // 전체 할인 쿠폰
    @Transactional(readOnly = false)
    fun processTotalDiscountCouponStep(orderTotalDiscountCouponStepDto: OrderTotalDiscountCouponStepDto): OrderTotalDiscountCouponStepDto {
        val couponEvent = CouponEvent(
            EventId(
                orderTotalDiscountCouponStepDto.orderId,
                EventType.PROCESS_TOTAL_DISCOUNT_COUPON
            )
        )

        couponEventRepository.save(couponEvent)

        val orderTotalDiscountCouponDtos = orderTotalDiscountCouponStepDto.orderTotalDiscountCouponDtos
        orderTotalDiscountCouponStepDto.state = "PENDING"

        val usedCouponsByProduct = mutableMapOf<String, Int>()
        var calculatedPrice = orderTotalDiscountCouponStepDto.totalPrice

        orderTotalDiscountCouponDtos.forEach { orderTotalDiscountCouponDtos ->
            orderTotalDiscountCouponDtos.state = "PENDING"

            val optionalCouponByUser = couponByUserRepository.findById(orderTotalDiscountCouponDtos.couponByUserId)

            if (optionalCouponByUser.isEmpty) {
                orderTotalDiscountCouponDtos.state = NotExistElementException::class.java.simpleName
                throw NotExistElementException("해당 쿠폰이 존재하지 않습니다.")
            }

            val selectedCouponByUser = optionalCouponByUser.get()
            calculatedPrice = selectedCouponByUser.use(calculatedPrice,orderTotalDiscountCouponDtos)
        }

        orderTotalDiscountCouponStepDto.calculatedPrice = calculatedPrice
        orderTotalDiscountCouponStepDto.state = "COMP"
        return orderTotalDiscountCouponStepDto
    }

    // TODO: 구현 예정
//    // 하나만 사용가능한 일반 전체 할인 쿠폰
//    @Transactional(readOnly = false)
//    fun processTotalDiscountCouponStep(orderTotalDiscountCouponStepDto: OrderTotalDiscountCouponStepDto): OrderProductDiscountCouponStepDto {
//        val orderTotalDiscountCouponDtos = orderTotalDiscountCouponStepDto.orderTotalDiscountCouponDtos
//        orderTotalDiscountCouponStepDto.state = "PENDING"
//
//        val usedCouponsByProduct = mutableMapOf<String, Int>()
//
//        orderTotalDiscountCouponDtos.forEach { orderTotalDiscountCouponDtos ->
//            orderTotalDiscountCouponDtos.state = "PENDING"
////            if(usedCouponsByProduct.getOrDefault(orderTotalDiscountCouponDtos.productId, -1) != -1) {
////                orderTotalDiscountCouponDtos.state = UsingCouponRuleViolationException::class.java.simpleName
////                throw UsingCouponRuleViolationException("하나의 상품에 하나의 쿠폰만 사용 가능합니다.")
////            }
//
////            val optionalCouponByUser = couponByUserRepository.findByUserId(orderProductDiscountCouponDto.userId)
//            val optionalCouponByUser = couponByUserRepository.findById(orderTotalDiscountCouponDtos.couponByUserId)
//
//            if (optionalCouponByUser.isEmpty) {
//                orderTotalDiscountCouponDtos.state = NotExistElementException::class.java.simpleName
//                throw NotExistElementException("해당 쿠폰이 존재하지 않습니다.")
//            }
//
//            val selectedCouponByUser = optionalCouponByUser.get()
//            selectedCouponByUser.use(orderTotalDiscountCouponDtos)
//        }
//
//        orderTotalDiscountCouponStepDto.state = "COMP"
//        return orderTotalDiscountCouponStepDto
//    }
}