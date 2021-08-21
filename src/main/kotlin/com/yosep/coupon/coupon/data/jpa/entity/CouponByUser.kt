package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.BaseEntity
import com.yosep.coupon.common.exception.NoHasCouponException
import com.yosep.coupon.coupon.data.jpa.dto.OrderDiscountCouponDto
import java.lang.RuntimeException
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "yos_coupon_owned_by_user")
class CouponByUser(
    @Id
    @Column(length = 20)
    val id: String,
    @Column(nullable = false)
    val userId: String,
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "coupon_id", nullable = true)
    val coupon: Coupon,
    @Enumerated(EnumType.STRING)
    var state: CouponState = CouponState.READY
) : BaseEntity() {
    fun use(orderDiscountCouponDto: OrderDiscountCouponDto): OrderDiscountCouponDto {
        if(this.state != CouponState.READY) {
            // exception
            throw RuntimeException("이미 사용한 쿠폰입니다.")
        }

        validateCouponDto(orderDiscountCouponDto)
        val coupon = this.coupon

        orderDiscountCouponDto.calculatedPrice = coupon.calculatePrice(orderDiscountCouponDto)
        this.state = CouponState.COMP

        return orderDiscountCouponDto
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

