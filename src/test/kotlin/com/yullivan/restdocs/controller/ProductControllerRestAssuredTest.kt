package com.yullivan.restdocs.controller

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration

@ExtendWith(RestDocumentationExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerRestAssuredTest {

    @LocalServerPort
    val port: Int = 0

    lateinit var spec: RequestSpecification

    @BeforeEach
    fun init(restDocumentation: RestDocumentationContextProvider) {
        RestAssured.port = port
        spec = RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation))
            .build()
    }

    @Test
    fun `상품 생성을 시도하면 성공한다`() {
        val params = CreateProductRequest("포카리스웨트", 1500)

        val response = RestAssured
            .given(this.spec).log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .filter(document("products/create"))
            .post("/v1/products")

        response.then().log().all()
            .statusCode(HttpStatus.CREATED.value())
    }

    @Test
    fun `상품 목록 조회를 요청하면 성공한다`() {
        val response = RestAssured
            .given(this.spec)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .filter(document("products/find-all"))
            .get("/v1/products")

        response.then()
            .statusCode(HttpStatus.OK.value())
    }
}
