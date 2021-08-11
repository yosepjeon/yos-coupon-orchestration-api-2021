package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.BaseEntity
import com.yosep.coupon.common.exception.InvalidPriceException
import com.yosep.coupon.common.exception.NotEqualDiscountAmountException
import com.yosep.coupon.common.exception.NotEqualDiscountPercentException
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Table(name = "yos_coupon")
abstract class Coupon(
    @Id
    @Column(length = 20)
    open val couponId: String? = null,
    @Column(nullable = false)
    open var name: @NotNull String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var editableState: @NotNull EditableState? = EditableState.OFF,
    @Embedded
    open val couponStock: CouponStock,
    @Column
    open var productId: String,
    @Embedded
    open val couponDiscount: CouponDiscount,
//    @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY)
//    open val couponByUsers: List<CouponByUser>,
    @Column(nullable = true)
    open var startTime: LocalDateTime?,
    @Column(nullable = true)
    open var endTime: LocalDateTime?
) : BaseEntity() {
    abstract fun calculatePrice(orderProductDiscountCouponDto: OrderProductDiscountCouponDto): Long
    abstract fun getCoupon(orderProductDiscountCouponDto: OrderProductDiscountCouponDto)

    protected fun validateCouponDto(orderProductDiscountCouponDto: OrderProductDiscountCouponDto) {
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

    fun validateCouponDtoNotPublishException(orderProductDiscountCouponDto: OrderProductDiscountCouponDto) {
        if (this.couponDiscount!!.discountAmount != orderProductDiscountCouponDto.discountAmount) {
            orderProductDiscountCouponDto.state = "NotEqualDiscountAmountException"
        }

        if (this.couponDiscount!!.discountPercent != orderProductDiscountCouponDto.discountPercent) {
            orderProductDiscountCouponDto.state = "NotEqualDiscountPercentException"
        }

        if (orderProductDiscountCouponDto.totalPrice < 0) {
            orderProductDiscountCouponDto.state = "InvalidPriceException"
        }
    }

//    abstract fun use(t: T)
}