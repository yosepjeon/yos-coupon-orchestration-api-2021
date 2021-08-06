package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.RandomIdGenerator.generate
import com.yosep.coupon.common.exception.InvalidStockValueException
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto
import javax.persistence.*
import javax.validation.constraints.NotNull

@Embeddable
class CouponStock(
    @field:Column(nullable = false)
    private var total: @NotNull Long = 0,
    @field:Column(nullable = false)
    private var remain: @NotNull Long = 0
){
    fun setRemain(remain: Long) {
        validateStock(remain)
        this.remain = remain
        total = remain
    }

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

//@Entity
//@Table(name = "yos_coupon_stock")
//class CouponStock(
//    @Id
//    private val id: String,
//    @field:Column(nullable = false)
//    private var total: @NotNull Long
//) : BaseEntity() {
//
//    @field:Column(nullable = false)
//    private var remain: @NotNull Long = 0
//    fun setRemain(remain: Long) {
//        validateStock(remain)
//        this.remain = remain
//        total = remain
//    }
//
//    fun addStock(value: Long) {
//        validateStock(value)
//        remain += value
//        total += remain
//    }
//
//    fun increase(value: Long) {
//        validateStock(value)
//        remain += value
//    }
//
//    fun decrease(value: Long) {
//        validateStock(value)
//        validateStock(remain - value)
//        remain -= value
//    }
//
////    fun decreaseStock(orderProductDtoForCreation: OrderProductDtoForCreation) {
////        val value: Long = orderProductDtoForCreation.getCount()
////        validateStock(orderProductDtoForCreation)
////        this.stockQuantity -= value
////    }
////
////    fun increaseStock(orderProductDtoForCreation: OrderProductDtoForCreation) {
////        validateStock(orderProductDtoForCreation.getCount())
////        this.stockQuantity += orderProductDtoForCreation.getCount()
////    }
////
////    fun validatePrice(orderProductDtoForCreation: OrderProductDtoForCreation) {
////        if (this.productPrice != orderProductDtoForCreation.getPrice()) {
////            orderProductDtoForCreation.setState("NotEqualProductPrice")
////            throw NotEqualProductPrice("해당 상품의 가격과 요청 데이터의 가격 값이 일치하지 않습니다.")
////        }
////    }
////
////    private open fun validateStock(orderProductDtoForCreation: OrderProductDtoForCreation) {
////        if (orderProductDtoForCreation.getCount() < 0L || this.stockQuantity - orderProductDtoForCreation.getCount() < 0L) {
////            orderProductDtoForCreation.setState("InvalidStockValueException")
////            throw InvalidStockValueException("0이상의 결과값이어야합니다.")
////        }
////    }
//
//    companion object {
//        fun of(total: Long): CouponStock {
//            val id = generate()
//            validateStock(total)
//            val couponStock = CouponStock("", 0)
//            couponStock.total = total
//            couponStock.remain = total
//            return couponStock
//        }
//
//        private fun validateStock(value: Long) {
//            if (value < 0L) {
//                throw InvalidStockValueException()
//            }
//        }
//    }
//}