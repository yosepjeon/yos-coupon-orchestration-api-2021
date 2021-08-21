package com.yosep.coupon.coupon.mq.consumer

import com.yosep.coupon.coupon.service.CouponCommandService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.io.IOException


@Service
class ConsumerFromProduct @Autowired constructor(
    private val couponCommandService: CouponCommandService
) {

    @KafkaListener(topics = ["exam"], groupId = "foo")
    @Throws(IOException::class)
    fun consume(message: String) {
        println(String.format("Consumed message : %s", message))
    }


}