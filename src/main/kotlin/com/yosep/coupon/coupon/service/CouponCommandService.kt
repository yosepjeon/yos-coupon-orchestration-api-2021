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
    fun deleteCouponById(couponId: String): Boolean {
        if (couponRepository.findById(couponId).isEmpty) {
            return false
        }

        couponRepository.deleteById(couponId)

        return true
    }
}