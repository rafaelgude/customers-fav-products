package com.rafaelgude.customersfavproducts.customers;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CustomerDTO {

    private Long id;

    private String name;

    private String email;

    public CustomerDTO(Customer customer) {
        super();
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
    }

}