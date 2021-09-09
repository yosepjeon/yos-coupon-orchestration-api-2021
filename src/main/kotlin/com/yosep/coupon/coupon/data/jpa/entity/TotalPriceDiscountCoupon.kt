package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.exception.InvalidPriceException
import com.yosep.coupon.common.exception.NotEqualDiscountAmountException
import com.yosep.coupon.common.exception.NotEqualDiscountPercentException
import com.yosep.coupon.coupon.data.jpa.dto.OrderDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.vo.CouponState
import java.time.LocalDateTime
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.validation.constraints.NotNull

@Entity
@DiscriminatorValue("Total")
class TotalPriceDiscountCoupon(
    couponId: String,
    name: @NotNull String,
    editableState: @NotNull EditableState? = EditableState.OFF,
    couponStock: CouponStock,
    couponDiscount: CouponDiscount,
    couponByUsers: List<CouponByUser>?,
    startTime: LocalDateTime?,
    endTime: LocalDateTime?,
    dtype: String?,
    state: CouponState?
) : Coupon(
    couponId,
    name,
    editableState,
    couponStock,
    couponDiscount,
    couponByUsers,
    startTime,
    endTime,
    dtype,
    state
) {
    override fun calculatePrice(totalPrice: Long): Long {
        return couponDiscount!!.calculateProductPrice(totalPrice)
    }

    override fun calculatePrice(orderDiscountCouponDto: OrderDiscountCouponDto): Long {
        validateCouponDto(orderDiscountCouponDto)

        return couponDiscount!!.calculateProductPrice(orderDiscountCouponDto.totalPrice)
    }

    override fun getCoupon(orderDiscountCouponDto: OrderDiscountCouponDto) {
        TODO("Not yet implemented")
    }

    // 불필요한 로직인듯...
    override fun validateCouponDto(orderDiscountCouponDto: OrderDiscountCouponDto) {
        if (this.couponDiscount!!.discountAmount != orderDiscountCouponDto.discountAmount) {
            orderDiscountCouponDto.state = "NotEqualDiscountAmountException"
            throw NotEqualDiscountAmountException("할인 금액이 맞지않습니다.")
        }

        if (this.couponDiscount!!.discountPercent != orderDiscountCouponDto.discountPercent) {
            orderDiscountCouponDto.state = "NotEqualDiscountPercentException"
            throw NotEqualDiscountPercentException("할인 비율이 맞지않습니다.")
        }

        if (orderDiscountCouponDto.totalPrice < 0) {
            orderDiscountCouponDto.state = "InvalidPriceException"
            throw InvalidPriceException("가격이 0 미만입니다.")
        }
    }

    override fun validateCouponDtoNotPublishException(orderDiscountCouponDto: OrderDiscountCouponDto) {
        if (this.couponDiscount!!.discountAmount != orderDiscountCouponDto.discountAmount) {
            orderDiscountCouponDto.state = "NotEqualDiscountAmountException"
        }

        if (this.couponDiscount!!.discountPercent != orderDiscountCouponDto.discountPercent) {
            orderDiscountCouponDto.state = "NotEqualDiscountPercentException"
        }

        if (orderDiscountCouponDto.totalPrice < 0) {
            orderDiscountCouponDto.state = "InvalidPriceException"
        }
    }
}