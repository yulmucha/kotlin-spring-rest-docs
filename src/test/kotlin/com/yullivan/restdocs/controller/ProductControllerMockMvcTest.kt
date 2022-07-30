package com.yullivan.restdocs.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureRestDocs
@WebMvcTest
class ProductControllerMockMvcTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `상품 생성을 시도하면 성공한다`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(CreateProductRequest("포카리스웨트", 1500)))
        ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value("포카리스웨트"))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.price").value(1500))
            .andDo(
                document(
                    "products/mockmvc-create",
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING)
                            .description("상품 이름"),
                        PayloadDocumentation.fieldWithPath("price").type(JsonFieldType.NUMBER)
                            .description("상품 가격")
                    )
                )
            )
    }

    @Test
    fun `상품 목록 조회를 요청하면 성공한다`() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/products")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "products/mockmvc-find-all"
                )
            )
    }
}
