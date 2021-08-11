package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto
import java.time.LocalDateTime
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.validation.constraints.NotNull

@Entity
@DiscriminatorValue("Total")
class TotalPriceDiscountCoupon(
    id: String,
    name: @NotNull String,
    state: @NotNull EditableState,
    couponStock: CouponStock,
    productId: String,
    couponDiscount: CouponDiscount,
//    couponByUsers: List<CouponByUser>,
    startTime: LocalDateTime,
    endTime: LocalDateTime
) : Coupon(
    id, name, state, couponStock, productId, couponDiscount, startTime, endTime
) {
    override fun calculatePrice(orderProductDiscountCouponDto: OrderProductDiscountCouponDto): Long {
        validateCouponDto(orderProductDiscountCouponDto)

        return 0
    }

    override fun getCoupon(orderProductDiscountCouponDto: OrderProductDiscountCouponDto) {
        TODO("Not yet implemented")
    }

}