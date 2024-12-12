package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.config.ServerAddressConfig;
import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.LoginDTO;
import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.entity.CustomerEntity;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.exception.custom.UserAlreadyExistsException;
import cz.jkdabing.backend.mapper.UserMapper;
import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.*;
import cz.jkdabing.backend.util.TableNameUtil;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserServiceImpl extends AbstractService implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CustomerService customerService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final EmailService emailService;

    private final ServerAddressConfig serverAddressConfig;

    public UserServiceImpl(
            MessageService messageService,
            AuditService auditService,
            UserRepository userRepository,
            UserMapper userMapper,
            CustomerService customerService,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            EmailService emailService,
            ServerAddressConfig serverAddressConfig
    ) {
        super(messageService, auditService);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
        this.serverAddressConfig = serverAddressConfig;
    }

    @Override
    public void createUser(UserDTO userDTO) {
        try {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            UserEntity userEntity = userMapper.toEntity(userDTO);
            userEntity.setActivationToken(UUID.randomUUID().toString());
            userRepository.save(userEntity);

            customerService.createCustomer(userEntity, userDTO.getCustomer());

            String userEmail = userDTO.getCustomer().getEmail();
            sendActivationEmail(userEmail, userEntity.getActivationToken());

            prepareAuditLog(
                    TableNameUtil.getTableName(userEntity.getClass()),
                    userEntity.getUserId(),
                    AuditLogConstants.ACTION_REGISTER
            );
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException(
                    getLocalizedMessage("error.user.username.already.exists", userDTO.getUsername())
            );
        }
    }

    @Override
    @Transactional
    public void activeUser(String token) {
        UserEntity userEntity = findUserByActivationToken(token);
        userEntity.setActive(true);
        userEntity.setActivationToken(null);

        userRepository.save(userEntity);

        prepareAuditLog(
                TableNameUtil.getTableName(userEntity.getClass()),
                userEntity.getUserId(),
                AuditLogConstants.ACTION_ACTIVATE
        );
    }

    @Override
    public String authenticateUser(@Valid LoginDTO loginDTO) {
        CustomerEntity customerEntity = customerService.getCustomerByUserNameOrThrow(loginDTO.getUsername());
        UserEntity userEntity = customerEntity.getUser();
        if (!userEntity.isEnabled()) {
            throw new BadCredentialsException(
                    getLocalizedMessage("error.user.not.active", loginDTO.getUsername())
            );
        }

        if (passwordEncoder.matches(loginDTO.getPassword(), userEntity.getPassword())) {
            Object rolesObj = userEntity.getRoles();

            if (rolesObj instanceof List<?> rolesList) {
                List<String> roles = rolesList.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .toList();

                return jwtTokenProvider.createUserToken(
                        userEntity.getTokenVersion(),
                        customerEntity.getCustomerId().toString(),
                        userEntity.getUserId().toString(),
                        userEntity.getUsername(),
                        roles);
            }

            throw new IllegalArgumentException(getLocalizedMessage("error.user.bad.roles"));
        } else {
            throw new BadCredentialsException(getLocalizedMessage("error.user.bad.credentials"));
        }
    }

    private UserEntity findUserByActivationToken(String token) {
        return userRepository.findByActivationToken(token)
                .orElseThrow(() -> new NoSuchElementException(
                        getLocalizedMessage("jwt.invalid.activation.token", token)
                ));
    }

    private void sendActivationEmail(String email, String activationToken) {
        String activationLink = serverAddressConfig.getServerAddress() + "api/users/activate?token=" + activationToken;
        emailService.sendActivationEmail(email, activationLink);
    }
}
