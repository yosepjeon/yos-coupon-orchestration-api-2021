package com.yosep.coupon.coupon.service

import com.yosep.coupon.common.data.RandomIdGenerator
import com.yosep.coupon.coupon.data.jpa.dto.CreatedTotalDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.entity.CouponState
import com.yosep.coupon.coupon.data.jpa.entity.EditableState
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
}