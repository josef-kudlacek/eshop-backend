package cz.jkdabing.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jkdabing.backend.BackendApplication;
import cz.jkdabing.backend.TestFactory;
import cz.jkdabing.backend.constant.CustomerTestConstants;
import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BackendApplication.class)
@AutoConfigureMockMvc
class CustomerControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CustomerControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void testRegisterCustomer_Errors() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .build();

        String customerJson = objectMapper.writeValueAsString(customerDTO);

        mockMvc.perform(post("/api/customers")
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
                .andExpect(jsonPath("$.description").value("uri=/api/customers"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testRegisterCustomer_Success() throws Exception {
        UUID customerId = CustomerTestConstants.ID;
        String token = CustomerTestConstants.TOKEN;

        when(customerService.createCustomer(Mockito.any(CustomerDTO.class)))
                .thenReturn(customerId);

        when(jwtTokenProvider.createCustomerToken(customerId.toString()))
                .thenReturn(token);

        CustomerDTO customerDTO = TestFactory.prepareCustomerDTO();

        String customerJson = objectMapper.writeValueAsString(customerDTO);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));

        Mockito.verify(customerService, times(1))
                .createCustomer(customerDTO);

        Mockito.verify(jwtTokenProvider, times(1))
                .createCustomerToken(customerId.toString());
    }
}