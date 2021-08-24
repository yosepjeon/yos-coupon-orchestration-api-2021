package com.yosep.coupon.coupon.data.jpa.vo

import com.querydsl.core.annotations.QueryProjection

class ProjectionCouponByUser1 @QueryProjection constructor(
    val id: Long,
    val name: String
) {
    override fun toString(): String {
        return "StaffVo(id=$id, name='$name')"
    }
}