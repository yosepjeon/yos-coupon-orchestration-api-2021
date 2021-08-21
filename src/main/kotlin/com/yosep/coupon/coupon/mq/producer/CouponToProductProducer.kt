package com.yosep.coupon.coupon.mq.producer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class CouponToProductProducer @Autowired constructor(
    private val kafkaTemplate: KafkaTemplate<Int, String>
) {
    
}