package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.constants.CustomerConstants;
import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.entity.CustomerEntity;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.exception.custom.BadRequestException;
import cz.jkdabing.backend.mapper.CustomerMapper;
import cz.jkdabing.backend.repository.CustomerRepository;
import cz.jkdabing.backend.service.AbstractService;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.CustomerService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.TableNameUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class CustomerServiceImpl extends AbstractService implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(
            MessageService messageService,
            AuditService auditService,
            CustomerRepository customerRepository,
            CustomerMapper customerMapper) {
        super(messageService, auditService);
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public void createCustomer(UserEntity userEntity, @Valid CustomerDTO customerDTO) {
        CustomerEntity customerEntity = customerMapper.toEntity(customerDTO);
        customerEntity.setUser(userEntity);

        customerRepository.save(customerEntity);
    }

    @Override
    public void createCustomer(@Valid CustomerDTO customerDTO, UUID customerId) {
        CustomerEntity customerEntity = findCustomerByCustomerIdOrThrow(customerId);
        if (!CustomerConstants.GUEST_NAME.equals(customerEntity.getLastName())) {
            throw new BadRequestException(getLocalizedMessage("error.customer.already.filled.billing.info"));
        }

        customerMapper.updateEntity(customerDTO, customerEntity);
        customerEntity.getAddresses().forEach(
                addressEntity -> addressEntity.setCustomer(customerEntity)
        );
        customerRepository.saveAndFlush(customerEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(customerEntity.getClass()),
                customerEntity.getCustomerId(),
                AuditLogConstants.ACTION_REGISTER
        );
    }

    @Override
    public CustomerEntity getCustomerByUserNameOrThrow(String username) {
        return customerRepository.findCustomerByUser_username(username)
                .orElseThrow(() -> new NoSuchElementException(
                                getLocalizedMessage("error.customer.not.found")
                        )
                );
    }

    private CustomerEntity findCustomerByCustomerIdOrThrow(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException(
                                getLocalizedMessage("error.customer.not.found")
                        )
                );
    }
}
