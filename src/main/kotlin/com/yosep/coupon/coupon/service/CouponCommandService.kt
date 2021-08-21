package com.yosep.coupon.coupon.service

import com.yosep.coupon.common.data.RandomIdGenerator
import com.yosep.coupon.common.exception.NotExistElementException
import com.yosep.coupon.common.exception.NotExistProductException
import com.yosep.coupon.coupon.data.jpa.dto.CreatedProductDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.CreatedTotalDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.entity.EditableState
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import com.yosep.coupon.mapper.CouponMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Lock
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException
import javax.persistence.LockModeType

@Service
@Transactional(readOnly = false)
class CouponCommandService @Autowired constructor(
    private val couponRepository: CouponRepository,
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

    @Transactional(
        readOnly = false,
        rollbackFor = [RuntimeException::class, NotExistElementException::class]
    )
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    fun ownCoupon(couponId: String) {

    }


    /*
    * 쿠폰 삭제
     */
    fun deleteCouponById(couponId: String) {
        if (couponRepository.findById(couponId).isEmpty) {
            return
        }

        couponRepository.deleteById(couponId)
    }

    fun createProductDiscountCouponForTest(couponDtoForCreation: ProductDiscountCouponDtoForCreation) {
        if (checkIsPresentProduct(couponDtoForCreation.productId) != true) {
            throw NotExistProductException("ID: ${couponDtoForCreation.productId} 상품이 존재하지 않습니다.")
        }

        val couponForCreation = CouponMapper.INSTANCE.dtoToEntity(couponDtoForCreation)
        couponForCreation.editableState = EditableState.ON
        couponRepository.save(couponForCreation)
    }

    fun createTotalDiscountCouponForTest(couponDtoForCreation: TotalDiscountCouponDtoForCreation) {
        val couponForCreation = CouponMapper.INSTANCE.dtoToEntity(couponDtoForCreation)
        couponForCreation.editableState = EditableState.ON
        couponRepository.save(couponForCreation)
    }

    private fun checkIsPresentProduct(productId: String): Boolean? {
        val response = restTemplate.exchange(
            "http://localhost:8001/products?id=$productId",
            HttpMethod.GET, null, Boolean::class.java
        )

        return response.body
    }
}