package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.exception.InvalidPriceException
import com.yosep.coupon.common.exception.NotEqualDiscountAmountException
import com.yosep.coupon.common.exception.NotEqualDiscountPercentException
import com.yosep.coupon.coupon.data.jpa.dto.OrderDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.validation.constraints.NotNull

@Entity
@DiscriminatorValue("Product")
class ProductDiscountCoupon(
    couponId: String,
    name: @NotNull String,
    state: @NotNull EditableState? = EditableState.OFF,
    couponStock: CouponStock,
    @Column(nullable = false)
    val productId: String,
    couponDiscount: CouponDiscount,
    startTime: LocalDateTime?,
    endTime: LocalDateTime?,
    dtype: String?
) : Coupon(
    couponId,
    name,
    state,
    couponStock,
    couponDiscount,
    startTime,
    endTime,
    dtype
) {
    override fun calculatePrice(OrderDiscountCouponDto: OrderDiscountCouponDto): Long {
        validateCouponDto(OrderDiscountCouponDto)

        return couponDiscount!!.calculateProductPrice(OrderDiscountCouponDto.totalPrice * OrderDiscountCouponDto.productCount)
    }

    override fun getCoupon(OrderDiscountCouponDto: OrderDiscountCouponDto) {

    }

    override fun validateCouponDto(orderProductDiscountCouponDto: OrderDiscountCouponDto) {
        if (this.couponDiscount!!.discountAmount != orderProductDiscountCouponDto.discountAmount) {
            orderProductDiscountCouponDto.state = "NotEqualDiscountAmountException"
            throw NotEqualDiscountAmountException("할인 금액이 맞지않습니다.")
        }

        if (this.couponDiscount!!.discountPercent != orderProductDiscountCouponDto.discountPercent) {
            orderProductDiscountCouponDto.state = "NotEqualDiscountPercentException"
            throw NotEqualDiscountPercentException("할인 비율이 맞지않습니다.")
        }

        if (orderProductDiscountCouponDto.totalPrice < 0) {
            orderProductDiscountCouponDto.state = "InvalidPriceException"
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