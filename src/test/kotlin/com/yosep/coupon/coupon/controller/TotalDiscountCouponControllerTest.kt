package com.yosep.coupon.coupon.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.yosep.coupon.coupon.data.jpa.dto.OrderTotalDiscountCouponDto
import com.yosep.coupon.coupon.data.jpa.dto.OrderTotalDiscountCouponStepDto
import com.yosep.coupon.coupon.data.jpa.dto.TotalDiscountCouponDtoForCreation
import com.yosep.coupon.coupon.data.jpa.repository.db.CouponByUserRepository
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
    private val wac: WebApplicationContext,
    private val couponByUserRepository: CouponByUserRepository
) {
    val log = Slf4JLoggerFactory.getInstance(TotalDiscountCouponControllerTest::class.java)

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilters<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true)) // ?????? ??????
            .build()
    }

    @Test
    @DisplayName("[Total Discount Coupon Controller] ?????????????????? ?????? ?????? ?????????")
    fun ??????_????????????_??????_??????_?????????() {
        log.info("[Total Discount Coupon Controller] ?????????????????? ?????? ?????? ?????????")

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
//    @DisplayName("[Total Discount Coupon Controller] ?????????????????? ?????? ?????? ?????????")
//    fun ??????????????????_??????_??????_??????_?????????() {
//        log.info("[Total Discount Coupon Controller] ?????????????????? ?????? ?????? ?????????")
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

    @Test
    @DisplayName("[Total Discount Coupon Controller] ?????? ?????? ?????? ?????? SAGA ?????? ?????? ?????????")
    fun ??????_??????_??????_??????_SAGA_??????_??????_?????????() {
        log.info("[Total Discount Coupon Controller] ?????? ?????? ?????? ?????? SAGA ?????? ?????? ?????????")

        val orderTotalDiscountCouponDtos = mutableListOf<OrderTotalDiscountCouponDto>()
        for(i in 1..2) {
            val orderTotalDiscountCouponDto = OrderTotalDiscountCouponDto(
            "TOTAL",
                "own-total-amount-coupon-test$i",
                "user-admin-for-test",
                10000,
                0,
                "READY"
            )

            orderTotalDiscountCouponDtos.add(orderTotalDiscountCouponDto)
        }

        val orderTotalDiscountCouponStepDto = OrderTotalDiscountCouponStepDto(
            "total-discount-coupon-test",
            100000,
            orderTotalDiscountCouponDtos,
            0,
            "READY"
        )

        val content = objectMapper.writeValueAsString(orderTotalDiscountCouponStepDto)

        log.info("###################### before ######################")
        orderTotalDiscountCouponDtos.forEach {
            val couponByUser = couponByUserRepository.findById(it.couponByUserId).get()
            log.info("${couponByUser.id}: ${couponByUser.state}")
        }

        mockMvc.perform(
            MockMvcRequestBuilders.post("/coupons/total/order-saga-total-coupon")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())

        log.info("###################### after ######################")
        orderTotalDiscountCouponDtos.forEach {
            val couponByUser = couponByUserRepository.findById(it.couponByUserId).get()
            log.info("${couponByUser.id}: ${couponByUser.state}")
        }
    }
}