package com.rafaelgude.customersfavproducts.customers;

import com.rafaelgude.customersfavproducts.config.BaseRestController;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController extends BaseRestController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Get customer by Id without favorite products")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @Operation(summary = "Get all customers without favorite products")
    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @Operation(summary = "Create a new customer")
    @PostMapping
    public ResponseEntity<CustomerDTO> create(@RequestBody @Valid CreateUpdateCustomerDTO createUpdateCustomerDTO) {
        var customerDto = customerService.create(createUpdateCustomerDTO.toEntity());
        return ResponseEntity.created(locationByEntity(customerDto.getId())).body(customerDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing customer")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @RequestBody @Valid CreateUpdateCustomerDTO createUpdateCustomerDTO) {
        return ResponseEntity.ok(customerService.update(id, createUpdateCustomerDTO.toEntity()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing customer")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/add-favorite-products")
    @Operation(summary = "Add favorite products to an existing customer")
    public ResponseEntity<Void> addFavoriteProducts(@PathVariable Long id, @RequestBody List<String> favoriteProductsId) {
        customerService.addFavoriteProducts(id, favoriteProductsId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/with-favorite-products")
    @Operation(summary = "Get customer by Id with favorite products")
    public ResponseEntity<CustomerWithFavoriteProductsDTO> findByIdWithFavoriteProducts(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findByIdWithFavoriteProducts(id));
    }

}
