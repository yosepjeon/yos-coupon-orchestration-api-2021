package com.yosep.coupon.common.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
@EnableKafka
class KafkaProducerConfig {
    @Bean
    fun producerFactory(): ProducerFactory<Int, String> {
        return DefaultKafkaProducerFactory(senderProps())
    }

    private fun senderProps(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[ProducerConfig.LINGER_MS_CONFIG] = 10
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.ACKS_CONFIG] = "all"
        //...
        return props
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<Int, String>): KafkaTemplate<Int, String> {
        return KafkaTemplate(producerFactory)
    }

//    @Bean
//    fun getProductToCouponSender(template: KafkaTemplate<Int?, String?>?): ProductToCouponProducer {
//        return ProductToCouponProducer(template)
//    }
}