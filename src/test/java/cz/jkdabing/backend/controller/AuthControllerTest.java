package cz.jkdabing.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Mock
    private CustomerService customerService;

    @Autowired
    public AuthControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void setUp() {
        Mockito.reset(customerService);
    }

    @Test
    public void testRegisterCustomer_Errors() throws Exception {
        Mockito.doNothing()
                .when(customerService)
                .registerCustomer(Mockito.any(CustomerDTO.class));

        CustomerDTO customerDTO = CustomerDTO.builder()
                .build();

        String customerJson = objectMapper.writeValueAsString(customerDTO);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.lastName").value("Last name may not be empty"))
                .andExpect(jsonPath("$.errors.firstName").value("First name may not be empty"))
                .andExpect(jsonPath("$.errors.country").value("Country may not be empty"))
                .andExpect(jsonPath("$.errors.city").value("City may not be empty"))
                .andExpect(jsonPath("$.errors.street").value("Street may not be empty"))
                .andExpect(jsonPath("$.errors.postalCode").value("Postal code may not be empty"))
                .andExpect(jsonPath("$.errors.email").value("Email may not be empty"))
                .andExpect(jsonPath("$.description").value("uri=/auth/register"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testRegisterCustomer_Success() throws Exception {
        Mockito.doNothing()
                .when(customerService)
                .registerCustomer(Mockito.any(CustomerDTO.class));

        CustomerDTO customerDTO = CustomerDTO.builder()
                .firstName("Jmeno")
                .lastName("Prijmeni")
                .email("jmeno.prijmeni@email.com")
                .street("Ulice 1")
                .city("Mesto")
                .postalCode("100 00")
                .country("Česká republika")
                .build();

        String customerJson = objectMapper.writeValueAsString(customerDTO);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer registered successfully"));
    }
}