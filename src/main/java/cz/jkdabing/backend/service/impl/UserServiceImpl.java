package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.constants.AuditLogConstants;
import cz.jkdabing.backend.dto.LoginDTO;
import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.exception.UserAlreadyExistsException;
import cz.jkdabing.backend.mapper.UserMapper;
import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.AuditService;
import cz.jkdabing.backend.service.CustomerService;
import cz.jkdabing.backend.service.EmailService;
import cz.jkdabing.backend.service.UserService;
import cz.jkdabing.backend.utils.TableNameUtil;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CustomerService customerService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final EmailService emailService;

    private final AuditService auditService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, CustomerService customerService,
                           PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, EmailService emailService,
                           AuditService auditService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
        this.auditService = auditService;
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

            auditService.prepareAuditLog(
                    TableNameUtil.getTableName(userEntity.getClass()),
                    userEntity.getUserId(),
                    AuditLogConstants.ACTION_REGISTER
            );
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException(String.format("Username '%s' already exists", userDTO.getUsername()));
        }
    }

    @Override
    @Transactional
    public void activeUser(String token) {
        UserEntity userEntity = findUserByActivationToken(token);
        userEntity.setActive(true);
        userEntity.setActivationToken(null);

        userRepository.save(userEntity);

        auditService.prepareAuditLog(
                TableNameUtil.getTableName(userEntity.getClass()),
                userEntity.getUserId(),
                AuditLogConstants.ACTION_ACTIVATE
        );
    }

    @Override
    public String authenticateUser(LoginDTO loginDTO) {
        UserEntity userEntity = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", loginDTO.getUsername())));

        if (!userEntity.isEnabled()) {
            throw new BadCredentialsException(String.format("User '%s' is not active", loginDTO.getUsername()));
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
                        userEntity.getUserId().toString(),
                        userEntity.getUsername(),
                        roles);
            }

            throw new IllegalArgumentException("Roles are not in the expected format");
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    private UserEntity findUserByActivationToken(String token) {
        return userRepository.findByActivationToken(token)
                .orElseThrow(() -> new NoSuchElementException("Invalid activation token: " + token));
    }

    private void sendActivationEmail(String email, String activationToken) {
        String activationLink = "http://localhost:8080/api/users/activate?token=" + activationToken;
        emailService.sendActivationEmail(email, activationLink);
    }
}
