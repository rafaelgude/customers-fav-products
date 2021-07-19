package com.rafaelgude.customersfavproducts.customers;

import com.rafaelgude.customersfavproducts.exceptions.EntityNotFoundException;
import com.rafaelgude.customersfavproducts.exceptions.NonUniqueEmailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.rafaelgude.customersfavproducts.MockUtil.mockCustomer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

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
        expected.setId(1L);

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
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(expected);
        when(customerRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(false);

        var actual = customerService.create(expected);

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void givenDuplicatedEmailCustomer_whenCreate_thenThrowNonUniqueEmailException() {
        var duplicatedEmailCustomer = mockCustomer();
        when(customerRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(true);

        assertThrows(NonUniqueEmailException.class, () -> customerService.create(duplicatedEmailCustomer));
    }

    @Test
    void givenCostumer_whenUpdate_thenUpdateCustomer() {
        var oldCustomer = mockCustomer();
        oldCustomer.setId(1L);

        var newCustomer = Customer.builder().id(1L).name("Fulano").email("fulano@example.com").build();

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(oldCustomer));
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(newCustomer);
        when(customerRepository.existsByEmailAndIdNot(any(String.class), any(Long.class))).thenReturn(false);

        var actual = customerService.update(1L, mockCustomer());

        assertEquals(newCustomer.getId(), actual.getId());
        assertEquals(newCustomer.getName(), actual.getName());
        assertEquals(newCustomer.getEmail(), actual.getEmail());
    }

    @Test
    void givenDuplicatedEmailCustomer_whenUpdate_thenThrowNonUniqueEmailException() {
        var customer = mockCustomer();
        customer.setId(1L);

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

}
