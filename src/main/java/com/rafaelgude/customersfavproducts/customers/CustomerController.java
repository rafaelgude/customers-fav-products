package com.rafaelgude.customersfavproducts.customers;

import com.rafaelgude.customersfavproducts.config.BaseRestController;
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

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> create(@RequestBody @Valid CreateUpdateCustomerDTO createUpdateCustomerDTO) {
        var customerDto = customerService.create(createUpdateCustomerDTO.toEntity());
        return ResponseEntity.created(locationByEntity(customerDto.getId())).body(customerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @RequestBody @Valid CreateUpdateCustomerDTO createUpdateCustomerDTO) {
        return ResponseEntity.ok(customerService.update(id, createUpdateCustomerDTO.toEntity()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/add-favorite-products")
    public ResponseEntity<Void> addFavoriteProducts(@PathVariable Long id, @RequestBody List<String> favoriteProductsId) {
        customerService.addFavoriteProducts(id, favoriteProductsId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/with-favorite-products")
    public ResponseEntity<CustomerWithFavoriteProductsDTO> findByIdWithFavoriteProducts(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findByIdWithFavoriteProducts(id));
    }

}
