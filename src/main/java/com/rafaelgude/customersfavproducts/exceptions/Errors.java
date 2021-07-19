package com.rafaelgude.customersfavproducts.exceptions;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Errors {

    private List<Error> errors;

}
