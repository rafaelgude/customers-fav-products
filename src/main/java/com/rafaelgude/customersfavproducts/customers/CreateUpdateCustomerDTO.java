package com.rafaelgude.customersfavproducts.customers;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CreateUpdateCustomerDTO {

    @NotBlank(message = "Name cannot be empty or null.")
    private String name;

    @NotBlank(message = "Email cannot be empty or null.")
    private String email;

    public Customer toEntity() {
        var customer = Customer.builder().build();
        BeanUtils.copyProperties(this, customer);

        return customer;
    }

}
