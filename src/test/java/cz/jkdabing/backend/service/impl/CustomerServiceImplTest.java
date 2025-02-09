package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.BackendApplication;
import cz.jkdabing.backend.TestFactory;
import cz.jkdabing.backend.constant.CustomerTestConstants;
import cz.jkdabing.backend.constants.CustomerConstants;
import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.entity.CustomerEntity;
import cz.jkdabing.backend.mapper.CustomerMapper;
import cz.jkdabing.backend.repository.CustomerRepository;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BackendApplication.class)
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private SecurityService securityService;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AuditService auditService;

    @Test
    void testCreateCustomer() {
        UUID customerId = CustomerTestConstants.CUSTOMER_ID_UUID;

        CustomerEntity originCustomerEntity = CustomerEntity
                .builder()
                .lastName(CustomerConstants.GUEST_NAME)
                .addresses(List.of(TestFactory.prepareAddressEntity()))
                .build();

        CustomerDTO customerDTO = TestFactory.prepareCustomerDTO();
        CustomerEntity customerEntity = TestFactory.prepareCustomerEntity();

        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(originCustomerEntity));

        customerService.createCustomer(customerDTO, customerEntity.getCustomerId());

        Mockito.verify(customerRepository, times(1))
                .findById(customerId);

        Mockito.verify(customerMapper, times(1))
                .updateEntity(customerDTO, originCustomerEntity);

        Mockito.verify(customerRepository, times(1))
                .saveAndFlush(originCustomerEntity);
    }
}