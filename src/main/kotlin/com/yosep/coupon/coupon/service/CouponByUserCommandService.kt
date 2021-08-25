package com.yosep.coupon.coupon.service

import com.yosep.coupon.common.data.RandomIdGenerator
import com.yosep.coupon.common.exception.NotExistElementException
import com.yosep.coupon.coupon.data.jpa.dto.CouponByUserCreationDto
import com.yosep.coupon.coupon.data.jpa.entity.CouponByUser
import com.yosep.coupon.coupon.data.jpa.entity.CouponState
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponByUserRepository
import com.yosep.coupon.data.jpa.repository.db.CouponRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Lock
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException
import javax.persistence.LockModeType

@Service
class CouponByUserCommandService @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val couponByUserRepository: CouponByUserRepository,
    private val restTemplate: RestTemplate
) {
    @Transactional(
        readOnly = false,
        rollbackFor = [RuntimeException::class, NotExistElementException::class]
    )
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    fun ownCoupon(couponByUserCreationDto: CouponByUserCreationDto): CouponByUser {
        val couponId = couponByUserCreationDto.couponId
        val optionalCoupon = couponRepository.findById(couponId)
        val response = restTemplate.exchange("http://localhost:8001/users?userId=${couponByUserCreationDto.userId}",
            HttpMethod.GET, null, Boolean::class.java)

        if(response.body == false) {
            throw RuntimeException("해당 유저아이디가 존재하지 않습니다.")
        }

        if (optionalCoupon.isEmpty) {
            throw NotExistElementException("쿠폰이 존재하지 않습니다.")
        }

        var id = ""
        if(couponByUserCreationDto.id == "" || couponByUserCreationDto.id.isEmpty()) {
            id = RandomIdGenerator.generate()
            while (couponByUserRepository.findById(id).isPresent) {
                id = RandomIdGenerator.generate()
            }
        }else {
            id = couponByUserCreationDto.id
        }

        val selectedCoupon = optionalCoupon.get()
        val couponByUser = CouponByUser(
            id,
            couponByUserCreationDto.userId,
            selectedCoupon,
            CouponState.READY,
        )

        selectedCoupon.couponStock.decrease(1)

        return couponByUserRepository.save(couponByUser)
    }

    fun deleteCouponByUser(id: String) {
        couponByUserRepository.deleteById(id)
    }
}