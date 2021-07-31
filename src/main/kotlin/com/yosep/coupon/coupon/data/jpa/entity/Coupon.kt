package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.BaseEntity
import com.yosep.coupon.coupon.data.jpa.dto.DiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.OrderCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDto
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
    open val id: String? = null,
    @Column(nullable = false)
    open var name: @NotNull String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var editableState: @NotNull EditableState = EditableState.OFF,
    @Embedded
    open val couponStock: CouponStock,
    @Column
    open var productId: String,
    @Embedded
    open val couponDiscount: CouponDiscount,
    @Column(nullable = true)
    open var startTime: LocalDateTime,
    @Column(nullable = true)
    open var endTime: LocalDateTime
) : BaseEntity() {
    abstract fun calculatePrice(orderCouponDtoForCreation: OrderCouponDtoForCreation): Long
    abstract fun useOneCoupon(orderCouponDtoForCreation: OrderCouponDtoForCreation)

    private fun validateCouponDto(orderCouponDtoForCreation: OrderCouponDtoForCreation) {
        if(this.couponDiscount.discountAmount != orderCouponDtoForCreation.discountAmount) {

        }

        if(this.couponDiscount.discountPercent != orderCouponDtoForCreation.discountPercent) {

        }
    }
//    abstract fun use(t: T)
}