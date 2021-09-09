package com.yosep.coupon.coupon.data.jpa.vo

import java.io.Serializable

data class EventId(
    private val eventId: String = "",
    private val eventType: EventType
) : Serializable {
    constructor() : this("", EventType.PROCESS_TOTAL_DISCOUNT_COUPON)
}