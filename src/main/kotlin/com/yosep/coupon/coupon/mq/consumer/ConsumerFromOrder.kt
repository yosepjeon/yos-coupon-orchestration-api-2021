package com.yosep.coupon.coupon.mq.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.yosep.coupon.coupon.data.event.RevertProductDiscountCouponStepEvent
import com.yosep.coupon.coupon.data.event.RevertTotalDiscountCouponStepEvent
import com.yosep.coupon.coupon.service.CouponCommandService
import com.yosep.coupon.coupon.service.CouponStepRevertService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.io.IOException
import kotlin.jvm.Throws

@Service
class ConsumerFromOrder @Autowired constructor(
    private val couponCommandService: CouponCommandService,
    private val couponStepRevertService: CouponStepRevertService,
    private val objectMapper: ObjectMapper
){
    @KafkaListener(topics = ["revert-product-discount-coupon-step"], groupId = "foo")
    @Throws(IOException::class)
    fun consumeRevertProductDiscountCouponEvent(message: String) {
        val revertProductDiscountCouponStepEvent = objectMapper.readValue(message, RevertProductDiscountCouponStepEvent::class.java)
        println(String.format("Consumed message: %s", revertProductDiscountCouponStepEvent))
        couponStepRevertService.revertProductDiscountCouponStep(revertProductDiscountCouponStepEvent)
    }

    @KafkaListener(topics = ["revert-total-discount-coupon-step"], groupId = "foo")
    @Throws(IOException::class)
    fun consumeRevertTotalDiscountCouponEvent(message: String) {
        val revertTotalDiscountCouponStepEvent = objectMapper.readValue(message, RevertTotalDiscountCouponStepEvent::class.java)
        println(String.format("Consumed message: %s", revertTotalDiscountCouponStepEvent))
        couponStepRevertService.revertTotalDiscountCouponStep(revertTotalDiscountCouponStepEvent)
    }
}