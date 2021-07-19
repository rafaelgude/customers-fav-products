package com.rafaelgude.customersfavproducts.customers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Column(unique = true)
    @NotBlank
    private String email;

    @Column(name = "product_id")
    @ElementCollection
    private Set<String> favoriteProductsId;

    @JsonIgnore
    public void addFavoriteProduct(String id) {
        this.favoriteProductsId.add(id);
    }

}
