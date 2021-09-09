package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.BaseEntity
import com.yosep.coupon.coupon.data.jpa.vo.EventId
import com.yosep.coupon.coupon.data.jpa.vo.EventType
import lombok.*
import org.springframework.data.domain.Persistable
import javax.persistence.*
import java.io.Serializable

@Entity
@Table(name = "yos_coupon_event")
class CouponEvent (
    @EmbeddedId
    private val eventId: EventId
) :BaseEntity(), Persistable<EventId> {

    override fun getId(): EventId? {
        return this.eventId
    }

    override fun isNew(): Boolean {
        return createdDate == null
    }
}