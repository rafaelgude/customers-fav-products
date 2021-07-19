package com.rafaelgude.customersfavproducts.products;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String id;

    private String title;

    private String image;

    private BigDecimal price;

    @JsonInclude(Include.NON_NULL)
    private Double reviewScore;

}
