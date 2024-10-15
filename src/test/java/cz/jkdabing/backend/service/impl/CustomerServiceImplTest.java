package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.BackendApplication;
import cz.jkdabing.backend.TestFactory;
import cz.jkdabing.backend.constant.CustomerTestConstants;
import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.entity.CustomerEntity;
import cz.jkdabing.backend.mapper.CustomerMapper;
import cz.jkdabing.backend.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BackendApplication.class)
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void testRegisterCustomer() {
        CustomerDTO customerDTO = TestFactory.prepareCustomerDTO();
        CustomerEntity customerEntity = TestFactory.prepareCustomerEntity();

        when(customerMapper.toEntity(customerDTO))
                .thenReturn(customerEntity);

        Long customerId = customerService.registerCustomer(customerDTO);

        assertEquals(CustomerTestConstants.ID, customerId);

        Mockito.verify(customerMapper, times(1))
                .toEntity(customerDTO);

        Mockito.verify(customerRepository, times(1))
                .saveAndFlush(customerEntity);
    }
}