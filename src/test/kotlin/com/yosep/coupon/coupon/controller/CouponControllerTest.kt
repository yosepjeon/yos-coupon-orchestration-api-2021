package com.yosep.coupon.coupon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import io.netty.util.internal.logging.Slf4JLoggerFactory
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class CouponControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {
    val log = Slf4JLoggerFactory.getInstance(CouponControllerTest::class.java)

    @Test
    @DisplayName("[Coupon Controller] 상품할인쿠폰 생성 테스트")
    fun 상품할인쿠폰_생성_테스트() {
        log.info("[Coupon Controller] 상품할인쿠폰 생성 테스트")

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
                post("/coupons/product")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    @DisplayName("[Coupon Controller] 전체할인쿠폰 생성 테스트")
    fun 전체할인쿠폰_생성_테스트() {
        log.info("[Coupon Controller] 전체할인쿠폰 생성 테스트")

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
                post("/coupons/total")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andDo(print())
    }
}