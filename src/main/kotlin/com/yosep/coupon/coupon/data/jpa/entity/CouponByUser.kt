package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.BaseEntity
import com.yosep.coupon.common.exception.StateChangeException
import com.yosep.coupon.coupon.data.jpa.dto.OrderCouponDtoForCreation
import java.util.function.Supplier
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
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private val coupon: Coupon,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private val editableState: @NotNull EditableState = EditableState.OFF,
    @Enumerated(EnumType.STRING)
    private val state: CouponState = CouponState.READY
) : BaseEntity() {
    private fun validateCouponDto(orderCouponDtoForCreation: OrderCouponDtoForCreation) {

        if(this.userId != orderCouponDtoForCreation.userId) {

        }
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