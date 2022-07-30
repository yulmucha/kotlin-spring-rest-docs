package com.yullivan.restdocs.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/v1/products")
class ProductRestController {

    var products = mutableListOf(
        Product(1, "코카콜라 제로", 1000),
        Product(2, "바나나 우유", 2000),
        Product(3, "삼다수", 500)
    )

    @PostMapping
    fun createProduct(@RequestBody request: CreateProductRequest): ResponseEntity<Product> {
        val newId: Long = (products.size + 1).toLong()
        val product = Product(newId, request.name, request.price)
        products.add(product)
        return ResponseEntity.created(URI.create("/v1/products/$newId")).body(product)
    }

    @GetMapping
    fun findProducts(): List<Product> {
        return products
    }
}

data class CreateProductRequest(
    val name: String,
    val price: Int
)

class Product(
    val id: Long,
    val name: String,
    val price: Int
)
