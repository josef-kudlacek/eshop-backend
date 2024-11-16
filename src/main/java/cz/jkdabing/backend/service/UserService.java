package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.UserDTO;

import java.util.UUID;

public interface UserService {

    void createUser(UserDTO userDTO);

    void activeUser(UUID userId);
}
