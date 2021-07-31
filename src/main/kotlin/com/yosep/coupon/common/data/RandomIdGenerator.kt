package com.yosep.coupon.common.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object RandomIdGenerator {
    fun generate(): String {
        val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 10)
        return now + uuid
    }
}