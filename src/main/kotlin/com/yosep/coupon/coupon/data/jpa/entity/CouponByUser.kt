package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.BaseEntity
import com.yosep.coupon.common.exception.NoHasCouponException
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto
import javax.persistence.*
import javax.validation.constraints.NotNull

//@ToString
//@Getter
//@EqualsAndHashCode(of = ["id"])
@Entity
@Table(name = "yos_coupon_owned_by_user")
class CouponByUser(
    @Id
    @Column(length = 20)
    private val id: String,
    @Column(nullable = false)
    private val userId: String,
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "coupon_id", nullable = true)
    private val coupon: Coupon,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private val editableState: @NotNull EditableState = EditableState.OFF,
    @Enumerated(EnumType.STRING)
    private var state: CouponState = CouponState.READY
) : BaseEntity() {
    fun use(orderProductDiscountCouponDto: OrderProductDiscountCouponDto): OrderProductDiscountCouponDto {
        validateCouponDto(orderProductDiscountCouponDto)
        val coupon = this.coupon

        orderProductDiscountCouponDto.calculatedPrice = coupon.calculatePrice(orderProductDiscountCouponDto)
        this.state = CouponState.COMP

        return orderProductDiscountCouponDto
    }

    private fun validateCouponDto(orderProductDiscountCouponDto: OrderProductDiscountCouponDto) {

        if (this.userId != orderProductDiscountCouponDto.userId) {
            orderProductDiscountCouponDto.state = "NoHasCouponException"
            throw NoHasCouponException("${orderProductDiscountCouponDto.userId}님은 해당 쿠폰을 가지고있지 않습니다.")
        }
    }

    fun validateCouponDtoNotPublishException(orderProductDiscountCouponDto: OrderProductDiscountCouponDto) {
        if (this.userId != orderProductDiscountCouponDto.userId) {
            orderProductDiscountCouponDto.state = "NoHasCouponException"
        }

        coupon.validateCouponDtoNotPublishException(orderProductDiscountCouponDto)
    }
}


//@ToString
//@Getter
//@EqualsAndHashCode(of = ["id"])
//@Entity
//@Table(name = "yos_coupon_owned_by_user")
//class CouponByUser : BaseEntity() {
//    @Id
//    @Column(length = 20)
//    private val id: String? = null
//
//    @Column(nullable = false)
//    private val userId: String? = null
//
//    @Column(nullable = false)
//    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
//    private val coupon: Coupon? = null
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private val state: @NotNull Coupon.State? = null
//
//    enum class State(
//        val isEditable: Boolean,
//        turnOnFunction: Supplier<Coupon.State>,
//        turnOffFunction: Supplier<Coupon.State>
//    ) {
//        READY(true,
//            Supplier<Coupon.State> { acceptOn() },
//            Supplier<Coupon.State> { acceptOff() }),
//        ON(false,
//            Supplier<Coupon.State> { notAccept() },
//            Supplier<Coupon.State> { acceptOff() }),
//        OFF(false,
//            Supplier<Coupon.State> { acceptOn() },
//            Supplier<Coupon.State> { notAccept() });
//
//        val turnOnFunction: Supplier<Coupon.State>
//        val turnOffFunction: Supplier<Coupon.State>
//        val isNotEditable: Boolean
//            get() = !isEditable
//
//        fun turnOn(): Coupon.State {
//            return turnOnFunction.get()
//        }
//
//        fun turnOff(): Coupon.State {
//            return turnOffFunction.get()
//        }
//
//        companion object {
//            private fun acceptOn(): Coupon.State {
//                return Coupon.State.ON
//            }
//
//            private fun acceptOff(): Coupon.State {
//                return Coupon.State.OFF
//            }
//
//            private fun notAccept(): Coupon.State {
//                throw StateChangeException()
//            }
//        }
//
//        init {
//            this.turnOnFunction = turnOnFunction
//            this.turnOffFunction = turnOffFunction
//        }
//    }
//}