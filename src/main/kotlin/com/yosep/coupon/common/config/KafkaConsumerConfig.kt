package com.yosep.coupon.common.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
@EnableKafka
class KafkaConsumerConfig {
    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<Int?, String?>): ConcurrentKafkaListenerContainerFactory<Int, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<Int, String>()
        factory.consumerFactory = consumerFactory
        return factory
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<Int, String> {
        return DefaultKafkaConsumerFactory(consumerProps())
    }

    private fun consumerProps(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[ConsumerConfig.GROUP_ID_CONFIG] = "group"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = IntegerDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        // ...
        return props
    }

//    @Bean
//    public ConsumerFactory.Listener listener() {
//        return new Listener ();
//    }
}
