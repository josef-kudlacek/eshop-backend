package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.mapper.UserMapper;
import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void createUser(UserDTO userDTO) {
        UserEntity userEntity = userMapper.toEntity(userDTO);

        userRepository.save(userEntity);
    }

    @Override
    public void activeUser(UUID userId) {
        UserEntity userEntity = findUserById(userId);
        userEntity.setActive(true);

        userRepository.save(userEntity);
    }

    private UserEntity findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
    }
}
