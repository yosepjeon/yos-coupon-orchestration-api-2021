package com.yosep.coupon.coupon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.yosep.coupon.common.exception.NoHasCouponException
import com.yosep.coupon.coupon.data.jpa.dto.ProductDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.vo.CouponDiscountVo
import com.yosep.coupon.coupon.data.jpa.vo.CouponStockVo
import com.yosep.coupon.coupon.service.CouponCommandService
import com.yosep.coupon.coupon.service.ProductDiscountCouponCommandService
import io.netty.util.internal.logging.Slf4JLoggerFactory
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.lang.RuntimeException


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
class CouponControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val couponCommandService: CouponCommandService,
    private val productDiscountCouponCommandService: ProductDiscountCouponCommandService,
) {
    val log = Slf4JLoggerFactory.getInstance(CouponControllerTest::class.java)
    var couponId: String = ""

    @BeforeEach
    fun setCoupons() {
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

        val createdCoupon = productDiscountCouponCommandService.createProductDiscountCoupon(couponDtoForCreation)
        couponId = createdCoupon.couponId
    }

    @Test
    @DisplayName("[Coupon Controller] 쿠폰 삭제 성공 테스트")
    fun 쿠폰_삭제_성공_테스트() {
        log.info("[Coupon Controller] 쿠폰 삭제 성공 테스트")
        log.info("created coupon id: $couponId")

        mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/coupons")
                    .param("id",couponId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @DisplayName("[Coupon Controller] 쿠폰 삭제 실패 테스트")
    fun 쿠폰_삭제_실패_테스트() {
        log.info("[Coupon Controller] 쿠폰 삭제 실패 테스트")
        log.info("created coupon id: $couponId")

        mockMvc
            .perform(
                MockMvcRequestBuilders.delete("/coupons")
                    .param("id","empty-coupon")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andDo(MockMvcResultHandlers.print())
    }
}