package com.yosep.coupon.common.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import javax.persistence.EntityManager

@EnableJpaAuditing
@Configuration
class MariaJpaConfig {
    @Bean
    fun jpaQueryFactory(em: EntityManager?): JPAQueryFactory {
        return JPAQueryFactory(em)
    }
}