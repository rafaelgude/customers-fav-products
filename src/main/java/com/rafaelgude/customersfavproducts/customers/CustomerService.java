package com.rafaelgude.customersfavproducts.customers;

import com.rafaelgude.customersfavproducts.exceptions.EntityNotFoundException;
import com.rafaelgude.customersfavproducts.exceptions.NonUniqueEmailException;
import com.rafaelgude.customersfavproducts.products.ProductDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.products.get-by-id.url}")
    private String productByIdUrl;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found."));
    }

    public CustomerDTO create(Customer customer) {
        customer.setId(0L);
        validate(customer);

        return new CustomerDTO(customerRepository.save(customer));
    }

    public CustomerDTO update(Long id, Customer newCustomer) {
        var customer = findById(id);
        BeanUtils.copyProperties(newCustomer, customer);
        customer.setId(id);
        validate(customer);

        return new CustomerDTO(customerRepository.save(customer));
    }

    public void delete(Long id) {
        customerRepository.delete(findById(id));
    }

    public void addFavoriteProducts(Long id, List<String> favoriteProductsId) {
        var customer = findById(id);

        favoriteProductsId.stream().forEach(productId -> {
            try {
                var productDto = restTemplate.getForObject(productByIdUrl + productId + "/", ProductDTO.class);
                customer.addFavoriteProduct(productDto.getId());
            } catch (HttpClientErrorException e) {
                if (e.getRawStatusCode() != HttpStatus.NOT_FOUND.value()) {
                    throw e;
                }
            }
        });

        customerRepository.save(customer);
    }

    public CustomerWithFavoriteProductsDTO findByIdWithFavoriteProducts(Long id) {
        var customer = findById(id);
        var customerDto = new CustomerWithFavoriteProductsDTO(customer);
        var productsToRemove = new HashSet<String>();

        customer.getFavoriteProductsId().forEach(productId -> {
            try {
                var productDto = restTemplate.getForObject(productByIdUrl + productId + "/", ProductDTO.class);
                customerDto.getFavoriteProducts().add(productDto);
            } catch (HttpClientErrorException e) {
                if (e.getRawStatusCode() != HttpStatus.NOT_FOUND.value()) {
                    throw e;
                }

                productsToRemove.add(productId);
            }
        });

        if (!productsToRemove.isEmpty()) {
            customer.getFavoriteProductsId().removeAll(productsToRemove);
            customerRepository.save(customer);
        }

        return customerDto;
    }

    private void validate(Customer customer) {
        var id = customer.getId() == null ? 0L : customer.getId();

        if (customerRepository.existsByEmailAndIdNot(customer.getEmail(), id)) {
            throw new NonUniqueEmailException("Email already exists.");
        }
    }

}
