package cz.jkdabing.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jkdabing.backend.BackendApplication;
import cz.jkdabing.backend.TestFactory;
import cz.jkdabing.backend.constant.CustomerTestConstants;
import cz.jkdabing.backend.constant.JwtTestConstants;
import cz.jkdabing.backend.constants.HttpHeaderConstants;
import cz.jkdabing.backend.constants.JWTConstants;
import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.CustomerService;
import cz.jkdabing.backend.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BackendApplication.class)
@AutoConfigureMockMvc
class CustomerControllerTest {

    public static final String CUSTOMER_API_URL = "/api/customers";

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private SecurityService securityService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CustomerControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void testRegisterCustomer_Unauthorized() throws Exception {
        String token = JwtTestConstants.INVALID_JWT_TOKEN;
        when(jwtTokenProvider.isTokenValid(token))
                .thenReturn(false);

        mockMvc.perform(post(CUSTOMER_API_URL)
                        .header(HttpHeaderConstants.AUTHORIZATION, JWTConstants.BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegisterCustomer_Errors() throws Exception {
        String token = JwtTestConstants.VALID_JWT_TOKEN;

        when(jwtTokenProvider.isTokenValid(token))
                .thenReturn(true);

        CustomerDTO customerDTO = CustomerDTO.builder()
                .build();

        String customerJson = objectMapper.writeValueAsString(customerDTO);

        mockMvc.perform(post(CUSTOMER_API_URL)
                        .header(HttpHeaderConstants.AUTHORIZATION, JWTConstants.BEARER + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Validace se nezdařila"))
                .andExpect(jsonPath("$.errors.lastName").value("Příjmení nesmí být prázdné"))
                .andExpect(jsonPath("$.errors.firstName").value("Křestní jméno nesmí být prázdné"))
                .andExpect(jsonPath("$.errors.country").value("Název státu musí být vyplněn"))
                .andExpect(jsonPath("$.errors.city").value("Název města musí být vyplněn"))
                .andExpect(jsonPath("$.errors.street").value("Název ulice musí být vyplněn"))
                .andExpect(jsonPath("$.errors.postalCode").value("PSČ nesmí být prázdné"))
                .andExpect(jsonPath("$.errors.email").value("E-mail nesmí být prázdný"))
                .andExpect(jsonPath("$.description").value("uri=/api/customers"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testRegisterCustomer_Success() throws Exception {
        UUID customerIdUuid = CustomerTestConstants.CUSTOMER_ID_UUID;
        String customerId = CustomerTestConstants.CUSTOMER_ID;
        String customerJwtToken = JwtTestConstants.VALID_JWT_TOKEN;
        String paymentJwtToken = CustomerTestConstants.TOKEN;
        CustomerDTO customerDTO = TestFactory.prepareCustomerDTO();
        String customerJson = objectMapper.writeValueAsString(customerDTO);

        when(jwtTokenProvider.isTokenValid(customerJwtToken))
                .thenReturn(true);

        when(jwtTokenProvider.getSubjectIdFromToken(customerJwtToken))
                .thenReturn(CustomerTestConstants.CUSTOMER_ID);

        when(securityService.getCustomerId(JWTConstants.BEARER + customerJwtToken))
                .thenReturn(customerIdUuid);

        when(jwtTokenProvider.createPaymentToken(customerId))
                .thenReturn(paymentJwtToken);

        mockMvc.perform(post(CUSTOMER_API_URL)
                        .header(HttpHeaderConstants.AUTHORIZATION, JWTConstants.BEARER + customerJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                )
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaderConstants.AUTHORIZATION))
                .andExpect(header().string(HttpHeaderConstants.AUTHORIZATION, JWTConstants.BEARER + paymentJwtToken));

        Mockito.verify(jwtTokenProvider, times(1))
                .isTokenValid(customerJwtToken);

        Mockito.verify(jwtTokenProvider, times(1))
                .getSubjectIdFromToken(customerJwtToken);

        Mockito.verify(securityService, times(1))
                .getCustomerId(JWTConstants.BEARER + customerJwtToken);

        Mockito.verify(customerService, times(1))
                .createCustomer(customerDTO, customerIdUuid);

        Mockito.verify(jwtTokenProvider, times(1))
                .createPaymentToken(customerId);
    }
}