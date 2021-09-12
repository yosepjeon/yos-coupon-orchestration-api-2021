package com.yosep.coupon.coupon.data.jpa.vo

import java.io.Serializable
import javax.persistence.EnumType
import javax.persistence.Enumerated

data class EventId(
    private val eventId: String = "",
    @Enumerated(EnumType.STRING)
    private val eventType: EventType
) : Serializable {
    constructor() : this("", EventType.PROCESS_TOTAL_DISCOUNT_COUPON)
}