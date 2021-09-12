package com.yosep.coupon.coupon.service

import com.yosep.coupon.common.data.RandomIdGenerator
import com.yosep.coupon.common.exception.NotExistElementException
import com.yosep.coupon.common.exception.NotExistProductException
import com.yosep.coupon.common.exception.UsingCouponRuleViolationException
import com.yosep.coupon.coupon.data.jpa.dto.CreatedProductDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
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
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Service
@Transactional(readOnly = false)
class ProductDiscountCouponCommandService @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val couponByUserRepository: CouponByUserRepository,
    private val couponEventRepository: CouponEventRepository,
    private val restTemplate: RestTemplate
) {
    /*
    * 상품 할인 쿠폰 생성
    * Logic:
    *
     */
    fun createProductDiscountCoupon(productDiscountCouponDtoForCreation: ProductDiscountCouponDtoForCreation): CreatedProductDiscountCouponDto {
        if (checkIsPresentProduct(productDiscountCouponDtoForCreation.productId) != true) {
            throw NotExistProductException("ID: ${productDiscountCouponDtoForCreation.productId} 상품이 존재하지 않습니다.")
        }

        var couponId = RandomIdGenerator.generate()
        while (couponRepository.findById(couponId).isPresent) {
            couponId = RandomIdGenerator.generate()
        }

        productDiscountCouponDtoForCreation.couponId = couponId

        val couponForCreation = CouponMapper.INSTANCE.dtoToEntity(productDiscountCouponDtoForCreation)
        couponForCreation.editableState = EditableState.ON

        val createdCoupon = couponRepository.save(couponForCreation)

        return CouponMapper.INSTANCE.entityToDto(createdCoupon)
    }

    fun createProductDiscountCouponForTest(couponDtoForCreation: ProductDiscountCouponDtoForCreation) {
        if (checkIsPresentProduct(couponDtoForCreation.productId) != true) {
            throw NotExistProductException("ID: ${couponDtoForCreation.productId} 상품이 존재하지 않습니다.")
        }

        val couponForCreation = CouponMapper.INSTANCE.dtoToEntity(couponDtoForCreation)
        couponForCreation.editableState = EditableState.ON
        couponForCreation.state = CouponState.READY
        couponRepository.save(couponForCreation)
    }

    @Transactional(readOnly = false)
    fun processProductDiscountCouponStep(orderProductDiscountCouponStepDto: OrderProductDiscountCouponStepDto): OrderProductDiscountCouponStepDto {
        val couponEvent = CouponEvent(
            EventId(
                orderProductDiscountCouponStepDto.orderId,
                EventType.PROCESS_PRODUCT_DISCOUNT_COUPON
            )
        )

        couponEventRepository.save(couponEvent)

        val orderProductDiscountCouponDtos = orderProductDiscountCouponStepDto.orderProductDiscountCouponDtos
        orderProductDiscountCouponStepDto.state = "PENDING"

        val usedCouponsByProduct = mutableMapOf<String, Int>()

        orderProductDiscountCouponDtos.forEach { orderProductDiscountCouponDto ->
            orderProductDiscountCouponDto.state = "PENDING"
            if(usedCouponsByProduct.getOrDefault(orderProductDiscountCouponDto.productId, -1) != -1) {
                orderProductDiscountCouponDto.state = UsingCouponRuleViolationException::class.java.simpleName
                throw UsingCouponRuleViolationException("하나의 상품에 하나의 쿠폰만 사용 가능합니다.")
            }

            val optionalCouponByUser = couponByUserRepository.findById(orderProductDiscountCouponDto.couponByUserId)

            if (optionalCouponByUser.isEmpty) {
                orderProductDiscountCouponDto.state = NotExistElementException::class.java.simpleName
                throw NotExistElementException("해당 쿠폰이 존재하지 않습니다.")
            }

            val selectedCouponByUser = optionalCouponByUser.get()
            selectedCouponByUser.use(orderProductDiscountCouponDto)
            usedCouponsByProduct[orderProductDiscountCouponDto.productId] = 1
        }

        orderProductDiscountCouponStepDto.state = "COMP"
        return orderProductDiscountCouponStepDto
    }

    private fun checkIsPresentProduct(productId: String): Boolean? {
        val response = restTemplate.exchange(
            "http://localhost:8001/products?id=$productId",
            HttpMethod.GET, null, Boolean::class.java
        )

        return response.body
    }
}