package com.rafaelgude.customersfavproducts;

import com.rafaelgude.customersfavproducts.customers.Customer;

public class MockUtil {

    public static Customer mockCustomer() {
        return Customer.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .build();
    }

}
