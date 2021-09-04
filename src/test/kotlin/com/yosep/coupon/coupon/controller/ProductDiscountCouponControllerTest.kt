package com.yosep.coupon.coupon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.OrderProductDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponByUserRepository
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class ProductDiscountCouponControllerTest @Autowired constructor(
    private var mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val wac: WebApplicationContext,
    private val couponByUserRepository: CouponByUserRepository
) {
    val log = Slf4JLoggerFactory.getInstance(ProductDiscountCouponControllerTest::class.java)

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilters<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true)) // 필터 추가
            .build()
    }

    @Test
    @DisplayName("[Product Discount Coupon Controller] 상품할인쿠폰 생성 성공 테스트")
    fun 상품할인쿠폰_생성_성공_테스트() {
        log.info("[Product Discount Coupon Controller] 상품할인쿠폰 생성 성공 테스트")

        val couponDtoForCreation = ProductDiscountCouponDtoForCreation(
            "",
            "coupon1",
            CouponStockVo(100, 100),
            "test-product-category1-0",
            CouponDiscountVo(
                10000,
                0
            )
        )

        val content = objectMapper.writeValueAsString(couponDtoForCreation)

        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/coupons/product")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @DisplayName("[Product Discount Coupon Controller] 상품할인쿠폰 생성 실패 테스트")
    fun 상품할인쿠폰_생성_실패_테스트() {
        log.info("[Product Discount Coupon Controller] 상품할인쿠폰 생성 실패 테스트")

        val couponDtoForCreation = ProductDiscountCouponDtoForCreation(
            "",
            "coupon1",
            CouponStockVo(100, 100),
            "empty-product",
            CouponDiscountVo(
                10000,
                0
            )
        )

        val content = objectMapper.writeValueAsString(couponDtoForCreation)

        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/coupons/product")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @DisplayName("[Product Discount Coupon Controller] 주문 상품 금액 할인 SAGA 처리 성공 테스트")
    fun 주문_상품_금액_할인_SAGA_처리_성공_테스트() {
        log.info("[Product Discount Coupon Controller] 주문 상품 금액 할인 SAGA 처리 성공 테스트")
        val orderProductDiscountCouponDtos = mutableListOf<OrderProductDiscountCouponDto>()

        // 금액 할인 쿠폰 사용
        for (i in 1..2) {
            val orderProductDiscountCouponDto = OrderProductDiscountCouponDto(
                "PRODUCT",
                "own-product-amount-coupon-test$i",
                1L * i,
                "user-admin-for-test",
                10000,
                0,
                "test-product-category1-$i",
                i * 111000L,
                0,
                "READY"
            )

            orderProductDiscountCouponDtos.add(orderProductDiscountCouponDto)
        }


        val orderProductDiscountCouponStepDto = OrderProductDiscountCouponStepDto(
            orderProductDiscountCouponDtos,
            "READY"
        )

        val content = objectMapper.writeValueAsString(orderProductDiscountCouponStepDto)

        log.info("###################### before ######################")
        orderProductDiscountCouponDtos.forEach {
            val couponByUser = couponByUserRepository.findById(it.couponByUserId).get()
            log.info("${couponByUser.id}: ${couponByUser.state}")
        }

        mockMvc.perform(
            post("/coupons/product/order-saga-product-coupon")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())

        log.info("###################### after ######################")
        orderProductDiscountCouponDtos.forEach {
            val couponByUser = couponByUserRepository.findById(it.couponByUserId).get()
            log.info("${couponByUser.id}: ${couponByUser.state}")
        }
    }

    @Test
    @DisplayName("[Product Discount Coupon Controller] 주문 상품 비율 할인 SAGA 처리 성공 테스트")
    fun 주문_상품_비율_할인_SAGA_처리_성공_테스트() {
        log.info("[Product Discount Coupon Controller] 주문 상품 비율 할인 SAGA 처리 성공 테스트")
        val orderProductDiscountCouponDtos = mutableListOf<OrderProductDiscountCouponDto>()

        // 비율 할인 쿠폰 사용
        for (i in 1..2) {
            val orderProductDiscountCouponDto = OrderProductDiscountCouponDto(
                "PRODUCT",
                "own-product-percent-coupon-test$i",
                1L * i,
                "user-admin-for-test",
                0,
                10L * (i+1),
                "test-product-category1-$i",
                i * 111000L,
                0,
                "READY"
            )

            orderProductDiscountCouponDtos.add(orderProductDiscountCouponDto)
        }


        val orderProductDiscountCouponStepDto = OrderProductDiscountCouponStepDto(
            orderProductDiscountCouponDtos,
            "READY"
        )

        val content = objectMapper.writeValueAsString(orderProductDiscountCouponStepDto)

        log.info("###################### before ######################")
        orderProductDiscountCouponDtos.forEach {
            val couponByUser = couponByUserRepository.findById(it.couponByUserId).get()
            log.info("${couponByUser.id}: ${couponByUser.state}")
        }

        mockMvc.perform(
            post("/coupons/product/order-saga-product-coupon")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())

        log.info("###################### after ######################")
        orderProductDiscountCouponDtos.forEach {
            val couponByUser = couponByUserRepository.findById(it.couponByUserId).get()
            log.info("${couponByUser.id}: ${couponByUser.state}")
        }
    }

    @Test
    @DisplayName("[Product Discount Coupon Controller] 주문 상품할인 SAGA 처리 성공 테스트")
    fun 주문_상품_할인_SAGA_처리_성공_테스트() {
        log.info("[Product Discount Coupon Controller] 주문 상품할인 SAGA 처리 성공 테스트")
        val orderProductDiscountCouponDtos = mutableListOf<OrderProductDiscountCouponDto>()

        // 금액 할인 쿠폰 사용
        for (i in 1..2) {
            val orderProductDiscountCouponDto = OrderProductDiscountCouponDto(
                "PRODUCT",
                "own-product-amount-coupon-test$i",
                1L * i,
                "user-admin-for-test",
                10000,
                0,
                "test-product-category1-$i",
                i * 111000L,
                0,
                "READY"
            )

            orderProductDiscountCouponDtos.add(orderProductDiscountCouponDto)
        }

        // 비율 할인 쿠폰 사용
        for (i in 1..2) {
            val orderProductDiscountCouponDto = OrderProductDiscountCouponDto(
                "PRODUCT",
                "own-product-percent-coupon-test$i",
                1L * i,
                "user-admin-for-test",
                0,
                10L * (i+1),
                "test-product-category1-$i",
                i * 111000L,
                0,
                "READY"
            )

            orderProductDiscountCouponDtos.add(orderProductDiscountCouponDto)
        }


        val orderProductDiscountCouponStepDto = OrderProductDiscountCouponStepDto(
            orderProductDiscountCouponDtos,
            "READY"
        )

        val content = objectMapper.writeValueAsString(orderProductDiscountCouponStepDto)

        log.info("###################### before ######################")
        orderProductDiscountCouponDtos.forEach {
            val couponByUser = couponByUserRepository.findById(it.couponByUserId).get()
            log.info("${couponByUser.id}: ${couponByUser.state}")
        }

        mockMvc.perform(
            post("/coupons/product/order-saga-product-coupon")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())

        log.info("###################### after ######################")
        orderProductDiscountCouponDtos.forEach {
            val couponByUser = couponByUserRepository.findById(it.couponByUserId).get()
            log.info("${couponByUser.id}: ${couponByUser.state}")
        }
    }
}