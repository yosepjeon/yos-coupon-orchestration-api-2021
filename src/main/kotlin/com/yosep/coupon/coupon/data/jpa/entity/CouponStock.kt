package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.RandomIdGenerator.generate
import com.yosep.coupon.common.exception.InvalidStockValueException
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto
import javax.persistence.*
import javax.validation.constraints.NotNull

@Embeddable
class CouponStock(
    @field:Column(nullable = false)
    var total: @NotNull Long = 0,
    @field:Column(nullable = false)
    var remain: @NotNull Long = 0,
){
    fun a() {
        println("total: $total")
        println("remain: $remain")
    }

//    fun setRemainValue(remain: Long) {
//        validateStock(remain)
//        this.remain = remain
//        total = remain
//    }

    fun addStock(value: Long) {
        validateStock(value)
        remain += value
        total += remain
    }

    fun increase(value: Long) {
        validateStock(value)
        remain += value
    }

    fun decrease(value: Long) {
        validateStock(value)
        validateStock(remain - value)
        remain -= value
    }

    fun decreaseStock(orderProductDiscountCouponDto: OrderProductDiscountCouponDto) {
        val value: Long = orderProductDiscountCouponDto.productCount
        validateStock(orderProductDiscountCouponDto)
        this.remain -= value
    }

    fun increaseStock(orderProductDiscountCouponDto: OrderProductDiscountCouponDto) {
        validateStock(orderProductDiscountCouponDto)
        this.remain += orderProductDiscountCouponDto.productCount
    }

    private fun validateStock(orderProductDiscountCouponDto: OrderProductDiscountCouponDto) {
        if (orderProductDiscountCouponDto.productCount < 0L || this.remain - orderProductDiscountCouponDto.productCount < 0L) {
            orderProductDiscountCouponDto.state = "InvalidStockValueException"
            throw InvalidStockValueException("0이상의 결과값이어야합니다.")
        }
    }

    companion object {
        fun of(total: Long): CouponStock {
            val id = generate()
            validateStock(total)
            val couponStock = CouponStock(total, total)
            couponStock.total = total
            couponStock.remain = total
            return couponStock
        }

        private fun validateStock(value: Long) {
            if (value < 0L) {
                throw InvalidStockValueException("0이상의 결과값이어야합니다.")
            }
        }
    }
}