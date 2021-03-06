package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.BaseEntity
import com.yosep.coupon.coupon.data.jpa.dto.OrderDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.vo.CouponState
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@Table(name = "yos_coupon")
abstract class Coupon(
    @Id
    @Column(length = 50)
    open val couponId: String,
    @Column(nullable = false)
    open var name: @NotNull String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    open var editableState: @NotNull EditableState? = EditableState.OFF,
    @Embedded
    open val couponStock: CouponStock,
    @Embedded
    open val couponDiscount: CouponDiscount,
    @OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open val couponByUsers: List<CouponByUser>? = emptyList(),
    @Column(nullable = true)
    open var startTime: LocalDateTime?,
    @Column(nullable = true)
    open var endTime: LocalDateTime?,
    @Column(insertable = false, updatable = false) //읽기 전용으로 선언
    open val dtype: String?,
    @Enumerated(EnumType.STRING)
    open var state: CouponState? = CouponState.READY
) : BaseEntity() {

    abstract fun calculatePrice(orderDiscountCouponDto: Long): Long
    abstract fun calculatePrice(orderDiscountCouponDto: OrderDiscountCouponDto): Long
    abstract fun getCoupon(orderDiscountCouponDto: OrderDiscountCouponDto)

    abstract fun validateCouponDto(orderDiscountCouponDto: OrderDiscountCouponDto)
    abstract fun validateCouponDtoNotPublishException(orderDiscountCouponDto: OrderDiscountCouponDto)

}