package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.CustomerDTO;
import cz.jkdabing.backend.entity.CustomerEntity;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.mapper.CustomerMapper;
import cz.jkdabing.backend.repository.CustomerRepository;
import cz.jkdabing.backend.service.AbstractService;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.CustomerService;
import cz.jkdabing.backend.service.MessageService;
import cz.jkdabing.backend.util.TableNameUtil;
import org.springframework.stereotype.Service;

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
    public void createCustomer(UserEntity userEntity, CustomerDTO customerDTO) {
        CustomerEntity customerEntity = customerMapper.toEntity(customerDTO);
        customerEntity.setUser(userEntity);

        customerRepository.save(customerEntity);
    }

    @Override
    public UUID createCustomer(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = customerMapper.toEntity(customerDTO);
        customerRepository.saveAndFlush(customerEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(customerEntity.getClass()),
                customerEntity.getCustomerId(),
                AuditLogConstants.ACTION_REGISTER
        );

        return customerEntity.getCustomerId();
    }
}
