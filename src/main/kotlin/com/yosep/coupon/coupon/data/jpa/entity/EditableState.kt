package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.exception.StateChangeException
import java.util.function.Supplier

enum class EditableState(
    val isEditable: Boolean,
    val turnOnFunction: Supplier<EditableState>,
    val turnOffFunction: Supplier<EditableState>
) {
    WAIT(true,
        Supplier { acceptOn() },
        Supplier { acceptOff() }),
    ON(false,
        Supplier { notAccept() },
        Supplier { acceptOff() }),
    OFF(false,
        Supplier { acceptOn() },
        Supplier { notAccept() });

    val isNotEditable: Boolean
        get() = !isEditable

    fun turnOn(): EditableState {
        return turnOnFunction.get()
    }

    fun turnOff(): EditableState {
        return turnOffFunction.get()
    }

    companion object {
        private fun acceptOn(): EditableState {
            return ON
        }

        private fun acceptOff(): EditableState {
            return OFF
        }

        private fun notAccept(): EditableState {
            throw StateChangeException()
        }
    }
}