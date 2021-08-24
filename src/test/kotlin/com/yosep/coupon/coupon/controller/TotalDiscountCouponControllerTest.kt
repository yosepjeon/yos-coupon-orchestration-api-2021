package com.yosep.coupon.coupon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
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
class TotalDiscountCouponControllerTest @Autowired constructor(
    private var mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val wac: WebApplicationContext
) {
    val log = Slf4JLoggerFactory.getInstance(TotalDiscountCouponControllerTest::class.java)

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilters<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true)) // 필터 추가
            .build()
    }

    @Test
    @DisplayName("[Total Discount Coupon Controller] 전체할인쿠폰 생성 성공 테스트")
    fun 전체할인쿠폰_생성_성공_테스트() {
        log.info("[Total Discount Coupon Controller] 전체할인쿠폰 생성 성공 테스트")

        val couponDtoForCreation = TotalDiscountCouponDtoForCreation(
            "",
            "coupon1",
            CouponStockVo(100, 100),
            CouponDiscountVo(
                10000,
                0
            )
        )

        val content = objectMapper.writeValueAsString(couponDtoForCreation)

        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/coupons/total")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

//    @Test
//    @DisplayName("[Total Discount Coupon Controller] 전체할인쿠폰 생성 실패 테스트")
//    fun 전체할인쿠폰_생성_성공_실패_테스트() {
//        log.info("[Total Discount Coupon Controller] 전체할인쿠폰 생성 실패 테스트")
//
//        val couponDtoForCreation = TotalDiscountCouponDtoForCreation(
//            "",
//            "coupon1",
//            CouponStockVo(100, 100),
//            CouponDiscountVo(
//                10000,
//                0
//            )
//        )
//
//        val content = objectMapper.writeValueAsString(couponDtoForCreation)
//
//        mockMvc
//            .perform(
//                MockMvcRequestBuilders.post("/coupons/total")
//                    .content(content)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .accept(MediaType.APPLICATION_JSON)
//            )
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andDo(MockMvcResultHandlers.print())
//    }
}