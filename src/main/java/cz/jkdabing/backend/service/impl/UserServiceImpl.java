package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.dto.LoginDTO;
import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.mapper.UserMapper;
import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.CustomerService;
import cz.jkdabing.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CustomerService customerService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, CustomerService customerService,
                           PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void createUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity userEntity = userMapper.toEntity(userDTO);
        userEntity.setActivationToken(UUID.randomUUID().toString());
        userRepository.save(userEntity);

        customerService.createCustomer(userEntity, userDTO.getCustomer());
    }

    @Override
    @Transactional
    public void activeUser(String token) {
        UserEntity userEntity = findUserByActivationToken(token);
        userEntity.setActive(true);
        userEntity.setActivationToken(null);
        userEntity.setUpdatedBy(userEntity);

        userRepository.save(userEntity);
    }

    @Override
    public String authenticateUser(LoginDTO loginDTO) {
        UserEntity userEntity = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!userEntity.isEnabled()) {
            throw new BadCredentialsException("User is not active");
        }

        if (passwordEncoder.matches(loginDTO.getPassword(), userEntity.getPassword())) {
            return jwtTokenProvider.createUserToken(userEntity);
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    private UserEntity findUserByActivationToken(String token) {
        return userRepository.findByActivationToken(token)
                .orElseThrow(() -> new NoSuchElementException("Invalid activation token: " + token));
    }
}
