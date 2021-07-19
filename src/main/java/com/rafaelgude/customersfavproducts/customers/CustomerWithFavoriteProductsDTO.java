package com.rafaelgude.customersfavproducts.customers;

import com.rafaelgude.customersfavproducts.products.ProductDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CustomerWithFavoriteProductsDTO {

    private Long id;

    private String name;

    private String email;

    private Set<ProductDTO> favoriteProducts;

    public CustomerWithFavoriteProductsDTO(Customer customer) {
        super();
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.favoriteProducts = new HashSet<>();
    }

}
