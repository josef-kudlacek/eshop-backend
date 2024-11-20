package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.LoginDTO;
import cz.jkdabing.backend.dto.UserDTO;
import cz.jkdabing.backend.entity.UserEntity;

public interface UserService {

    void createUser(UserDTO userDTO);

    void activeUser(String token);

    String authenticateUser(LoginDTO loginDTO);

    UserEntity getCurrentUser();
}
