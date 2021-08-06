package com.yosep.coupon.coupon.data.jpa

import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class aaaa {

    @Test
    fun assa() {
        val orderCouponDtoForCreations = mutableListOf<OrderProductDiscountCouponDto>()

        for(i in 1..10) {
            val orderCouponDtoForCreation = OrderProductDiscountCouponDto(
                "asd",
                "asb$i",
                3,
                "asdb",
                "asdb",
                0,
                0,
                "sadb",
                0,
                0,
            )

            orderCouponDtoForCreations.add(orderCouponDtoForCreation)
        }

        orderCouponDtoForCreations.forEach {
            it.state = "PENDING"
        }

        orderCouponDtoForCreations.forEach {
            println(it)
        }
    }
}