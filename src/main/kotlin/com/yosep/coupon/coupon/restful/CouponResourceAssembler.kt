package com.yosep.coupon.coupon.restful

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.stereotype.Component

@Component
class CouponResourceAssembler<T> : RepresentationModelAssembler<T, EntityModel<T>> {
    override fun toModel(entity: T): EntityModel<T> {
        TODO("Not yet implemented")
    }
}