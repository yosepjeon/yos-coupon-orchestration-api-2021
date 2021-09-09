package com.yosep.coupon.coupon.data.jpa.entity

import com.yosep.coupon.common.data.BaseEntity
import lombok.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = ["eventId"])
@Table(name = "yos_revert_total_coupon_step_event")
class RevertTotalDiscountCouponStepEvent (
    @Id
    @Column(length = 100)
    private var eventId: String
): BaseEntity()