package com.rafaelgude.customersfavproducts.customers;

import com.rafaelgude.customersfavproducts.products.ProductDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Set;

import static com.rafaelgude.customersfavproducts.MockUtil.mockCustomer;
import static com.rafaelgude.customersfavproducts.MockUtil.mockProdutoDTO;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindById() throws Exception {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(mockCustomer()));

        mockMvc.perform(get("/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindAll() throws Exception {
        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate() throws Exception {
        var body = "{\n" +
                "    \"name\": \"johndoe\",\n" +
                "    \"email\": \"johndoe@example.com\"\n" +
                "}";

        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer());

        mockMvc.perform(post("/customers")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdate() throws Exception {
        var expectedName = "Example";
        var expectedEmail = "example@example.com";

        var body = "{\n" +
                "    \"name\": \""+expectedName+"\",\n" +
                "    \"email\": \""+expectedEmail+"\"\n" +
                "}";

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(mockCustomer()));
        when(customerRepository.save(any(Customer.class))).thenReturn(Customer.builder().id(1L).name(expectedName).email(expectedEmail).build());

        mockMvc.perform(put("/customers/{id}", 1L)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.email").value(expectedEmail));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDelete() throws Exception {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(mockCustomer()));

        mockMvc.perform(delete("/customers/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddFavoriteProducts() throws Exception {
        var customer = mockCustomer();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(mockProdutoDTO());

        var productId = "h9f7s37v0h7d39n4n2m1";
        var customerWithFavProduct = mockCustomer();
        customerWithFavProduct.addFavoriteProduct(productId);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        var body = "[ \""+productId+"\" ]";

        mockMvc.perform(post("/customers/{id}/add-favorite-products", 1L)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindByIdWithFavoriteProducts() throws Exception {
        var productId = "h9f7s37v0h7d39n4n2m1";
        var productDTO = mockProdutoDTO();
        var customer = mockCustomer();
        customer.setFavoriteProductsId(Set.of(productId));

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(get("/customers/{id}/with-favorite-products", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.favoriteProducts").isArray())
                .andExpect(jsonPath("$.favoriteProducts").isNotEmpty())
                .andExpect(jsonPath("$.favoriteProducts[0].id").value(productId));
    }

}
