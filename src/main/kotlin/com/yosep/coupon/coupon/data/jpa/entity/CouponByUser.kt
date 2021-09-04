package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.BaseEntity
import com.yosep.coupon.common.exception.AlreadyUsedException
import com.yosep.coupon.common.exception.ExpireCouponException
import com.yosep.coupon.common.exception.NoHasCouponException
import com.yosep.coupon.coupon.data.jpa.dto.OrderDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.OrderTotalDiscountCouponDto
import java.lang.RuntimeException
import javax.persistence.*

@Entity
@Table(name = "yos_coupon_owned_by_user")
class CouponByUser(
    @Id
    @Column(length = 40)
    val id: String,
    @Column(nullable = false)
    val userId: String,
    @ManyToOne(cascade = [CascadeType.ALL, CascadeType.REMOVE])
    @JoinColumn(name = "coupon_id", nullable = true)
    val coupon: Coupon,
    @Enumerated(EnumType.STRING)
    var state: CouponState = CouponState.READY
) : BaseEntity() {
    fun getCouponByUserId(): String = this.id

    fun use(orderDiscountCouponDto: OrderDiscountCouponDto): OrderDiscountCouponDto {
        if(this.state == CouponState.EXPIRE) {
            // 만료 Exception
            orderDiscountCouponDto.state = ExpireCouponException::class.java.simpleName
            throw ExpireCouponException("만료된 쿠폰입니다.")
        }

        if(this.state == CouponState.COMP || this.state == CouponState.PENDING) {
            // exception
            orderDiscountCouponDto.state = AlreadyUsedException::class.java.simpleName
            this.state = CouponState.READY
            throw AlreadyUsedException("이미 사용한 쿠폰입니다.")
        }
        this.state = CouponState.PENDING

        validateCouponDto(orderDiscountCouponDto)
        val coupon = this.coupon

        orderDiscountCouponDto.calculatedPrice = coupon.calculatePrice(orderDiscountCouponDto)
        this.state = CouponState.COMP
        orderDiscountCouponDto.state = "COMP"

        return orderDiscountCouponDto
    }

    fun use(totalPrice:Long, orderDiscountCouponDto: OrderTotalDiscountCouponDto): Long {
        var totalPrice = totalPrice
        if(this.state == CouponState.EXPIRE) {
            // 만료 Exception
            orderDiscountCouponDto.state = ExpireCouponException::class.java.simpleName
            throw ExpireCouponException("만료된 쿠폰입니다.")
        }

        if(this.state == CouponState.COMP || this.state == CouponState.PENDING) {
            // exception
            orderDiscountCouponDto.state = AlreadyUsedException::class.java.simpleName
            this.state = CouponState.READY
            throw AlreadyUsedException("이미 사용한 쿠폰입니다.")
        }
        this.state = CouponState.PENDING

        validateCouponDto(orderDiscountCouponDto)
        val coupon = this.coupon

        this.state = CouponState.COMP
        orderDiscountCouponDto.state = "COMP"

        return coupon.calculatePrice(totalPrice)
    }

    private fun validateCouponDto(orderDiscountCouponDto: OrderTotalDiscountCouponDto) {

        if (this.userId != orderDiscountCouponDto.userId) {
            orderDiscountCouponDto.state = "NoHasCouponException"
            throw NoHasCouponException("${orderDiscountCouponDto.userId}님은 해당 쿠폰을 가지고있지 않습니다.")
        }
    }

    private fun validateCouponDto(orderDiscountCouponDto: OrderDiscountCouponDto) {

        if (this.userId != orderDiscountCouponDto.userId) {
            orderDiscountCouponDto.state = "NoHasCouponException"
            throw NoHasCouponException("${orderDiscountCouponDto.userId}님은 해당 쿠폰을 가지고있지 않습니다.")
        }
    }

    fun validateCouponDtoNotPublishException(orderDiscountCouponDto: OrderDiscountCouponDto) {
        if (this.userId != orderDiscountCouponDto.userId) {
            orderDiscountCouponDto.state = "NoHasCouponException"
        }

        coupon.validateCouponDtoNotPublishException(orderDiscountCouponDto)
    }

    fun revert() {
        this.state = CouponState.READY
    }
}

