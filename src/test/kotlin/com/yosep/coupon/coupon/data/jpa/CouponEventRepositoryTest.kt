package com.yosep.coupon.coupon.data.jpa

import com.yosep.coupon.coupon.data.jpa.entity.CouponEvent
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponEventRepository
import com.yosep.coupon.coupon.data.jpa.vo.EventId
import com.yosep.coupon.coupon.data.jpa.vo.EventType
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import javax.transaction.Transactional

@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class CouponEventRepositoryTest @Autowired constructor(
    private val couponEventRepository: CouponEventRepository
) {
    val log = Slf4JLoggerFactory.getInstance(CouponEventRepositoryTest::class.java)!!

    @BeforeEach
    fun setDate() {
        val eventId = EventId(
            "test2",
            EventType.PROCESS_TOTAL_DISCOUNT_COUPON
        )
        val event = CouponEvent(
            eventId
        )

        couponEventRepository.save(event)
    }

    @Test
    @DisplayName("[CouponEventRepository] Event 중복확인 테스트")
    fun event_중복확인_테스트() {
        log.info("[CouponEventRepository] Event 중복확인 테스트")
        val eventId1 = EventId(
        "test1",
        EventType.PROCESS_TOTAL_DISCOUNT_COUPON
        )
        val event1 = CouponEvent(
            eventId1
        )

        val createdEvent1 = couponEventRepository.save(event1)
        val eventId2 = EventId(
            "test2",
            EventType.PROCESS_TOTAL_DISCOUNT_COUPON
        )

        val event2 = CouponEvent(
            eventId2
        )

        Assertions.assertThrows(DataIntegrityViolationException::class.java) {
            couponEventRepository.save(event2)
        }
    }
}