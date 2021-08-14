package com.yosep.coupon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker

@SpringBootApplication
class YosCouponOrchestrationApi2021Application

fun main(args: Array<String>) {
	runApplication<YosCouponOrchestrationApi2021Application>(*args)
}
