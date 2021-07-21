package com.rafaelgude.customersfavproducts.customers;

import com.rafaelgude.customersfavproducts.exceptions.EntityNotFoundException;
import com.rafaelgude.customersfavproducts.exceptions.NonUniqueEmailException;
import com.rafaelgude.customersfavproducts.products.ProductDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.rafaelgude.customersfavproducts.MockUtil.mockCustomer;
import static com.rafaelgude.customersfavproducts.MockUtil.mockProdutoDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void whenFindAll_thenReturnListOfCustomers() {
        var expected = List.of(mockCustomer());
        when(customerRepository.findAll()).thenReturn(expected);

        var actual = customerService.findAll();

        assertEquals(expected.size(), actual.size());
    }

    @Test
    void whenFindById_thenReturnCustomer() {
        var expected = mockCustomer();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        var actual = customerService.findById(1L);

        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void whenFindByInvalidId_thenThrowEntityNotFoundException() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> customerService.findById(1L));
    }

    @Test
    void givenCostumer_whenCreate_thenSaveCustomer() {
        var expected = mockCustomer();
        expected.setId(null);

        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer());
        when(customerRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(false);

        var actual = customerService.create(expected);

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void givenDuplicatedEmailCustomer_whenCreate_thenThrowNonUniqueEmailException() {
        var duplicatedEmailCustomer = mockCustomer();
        duplicatedEmailCustomer.setId(null);

        when(customerRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(true);

        assertThrows(NonUniqueEmailException.class, () -> customerService.create(duplicatedEmailCustomer));
    }

    @Test
    void givenCostumer_whenUpdate_thenUpdateCustomer() {
        var oldCustomer = mockCustomer();
        var newCustomer = Customer.builder().id(1L).name("Jane Doe").email("janedoe@example.com").build();

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(oldCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);
        when(customerRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(false);

        var actual = customerService.update(1L, mockCustomer());

        assertEquals(newCustomer.getId(), actual.getId());
        assertEquals(newCustomer.getName(), actual.getName());
        assertEquals(newCustomer.getEmail(), actual.getEmail());
    }

    @Test
    void givenDuplicatedEmailCustomer_whenUpdate_thenThrowNonUniqueEmailException() {
        var customer = mockCustomer();

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(true);

        assertThrows(NonUniqueEmailException.class, () -> customerService.update(1L, mockCustomer()));
    }

    @Test
    void whenDelete_thenDeleteCostumer() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(mockCustomer()));
        doNothing().when(customerRepository).delete(any(Customer.class));

        assertDoesNotThrow(() -> customerService.delete(1L));
    }

    @Test
    void whenAddFavoriteProducts_thenAddFavoriteProductsOfCustomer() {
        var productDTO = mockProdutoDTO();
        var customer = mockCustomer();

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(productDTO);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.addFavoriteProducts(1L, List.of(productDTO.getId()));

        assertTrue(!customer.getFavoriteProductsId().isEmpty());
    }

    @Test
    void whenFindByIdWithFavoriteProducts_thenReturnCustomerWithFavoriteProducts() {
        var productDTO = mockProdutoDTO();
        var customer = mockCustomer();
        customer.setFavoriteProductsId(Set.of("h9f7s37v0h7d39n4n2m1"));

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(productDTO);

        var customerWithFavoriteProductsDTO = customerService.findByIdWithFavoriteProducts(1L);

        assertTrue(!customerWithFavoriteProductsDTO.getFavoriteProducts().isEmpty());
    }

}
