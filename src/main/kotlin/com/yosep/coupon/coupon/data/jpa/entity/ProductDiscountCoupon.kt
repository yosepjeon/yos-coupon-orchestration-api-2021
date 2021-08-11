package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto
import java.time.LocalDateTime
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.validation.constraints.NotNull

@Entity
@DiscriminatorValue("Product")
class ProductDiscountCoupon(
    couponId: String,
    name: @NotNull String,
    state: @NotNull EditableState?,
    couponStock: CouponStock,
    productId: String,
    couponDiscount: CouponDiscount,
    startTime: LocalDateTime?,
    endTime: LocalDateTime?,
) : Coupon(
    couponId,
    name,
    state,
    couponStock,
    productId,
    couponDiscount,
    startTime,
    endTime
) {
    override fun calculatePrice(orderProductDiscountCouponDto: OrderProductDiscountCouponDto): Long {
        validateCouponDto(orderProductDiscountCouponDto)

        return couponDiscount!!.calculateProductPrice(orderProductDiscountCouponDto.totalPrice * orderProductDiscountCouponDto.productCount)
    }

    override fun getCoupon(orderProductDiscountCouponDto: OrderProductDiscountCouponDto) {

    }
}