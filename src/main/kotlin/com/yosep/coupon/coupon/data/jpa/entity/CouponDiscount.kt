package com.yosep.coupon.coupon.data.jpa.entity

import javax.persistence.Embeddable

@Embeddable
class CouponDiscount(
    var discountAmount:Long = 0,
    var discountPercent:Long = 0
) {
    fun calculateProductPrice(productPrice: Long): Long {
        var value = productPrice
        value = calculateAmount(value)
        value = calculatePercent(value)
        return value
    }

    private fun calculateAmount(value: Long): Long {
        return if (value < discountAmount) 0 else value - discountAmount
    }

    private fun calculatePercent(value: Long): Long {
        return if (discountPercent <= 0) value else value / discountPercent / 100
    }
}