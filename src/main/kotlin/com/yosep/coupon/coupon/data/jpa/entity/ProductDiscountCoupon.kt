package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.coupon.data.jpa.dto.OrderCouponDtoForCreation
import java.time.LocalDateTime
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.validation.constraints.NotNull

@Entity
@DiscriminatorValue("Product")
class ProductDiscountCoupon(
    id: String,
    name: @NotNull String,
    state: @NotNull EditableState,
    couponStock: CouponStock,
    productId: String,
    couponDiscount: CouponDiscount,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
) : Coupon(
    id,
    name,
    state,
    couponStock,
    productId,
    couponDiscount,
    startTime,
    endTime
) {
    override fun calculatePrice(orderCouponDtoForCreation: OrderCouponDtoForCreation): Long {
        TODO("Not yet implemented")

    }

    override fun useOneCoupon(orderCouponDtoForCreation: OrderCouponDtoForCreation) {
        TODO("Not yet implemented")
    }
}