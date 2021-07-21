package com.rafaelgude.customersfavproducts;

import com.rafaelgude.customersfavproducts.customers.Customer;
import com.rafaelgude.customersfavproducts.products.ProductDTO;

import java.math.BigDecimal;
import java.util.HashSet;

public class MockUtil {

    public static Customer mockCustomer() {
        return Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("johndoe@example.com")
                .favoriteProductsId(new HashSet<>())
                .build();
    }

    public static ProductDTO mockProdutoDTO() {
        return ProductDTO.builder()
                .id("h9f7s37v0h7d39n4n2m1")
                .title("Title for ProductDTO")
                .image("http://site.com/image.png")
                .price(new BigDecimal("100.00"))
                .build();
    }

}
