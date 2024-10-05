package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.enums.UserRole;
import cz.jkdabing.backend.mapper.UserMapper;
import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.service.UserService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        UserEntity userEntity = userMapper.toEntity(userDTO);
        setUserEntity(userEntity);
        userRepository.save(userEntity);
    }

    private void setUserEntity(UserEntity userEntity) {
        userEntity.setActive(true);
        userEntity.setUserRole(UserRole.ROLE_REGISTERED);
        userEntity.setCreatedAt(ZonedDateTime.now());
        if (userEntity.getPassword() != null) {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }
    }
}
