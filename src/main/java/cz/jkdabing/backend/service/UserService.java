package cz.jkdabing.backend.service;

import cz.jkdabing.backend.dto.UserDTO;

public interface UserService {

    void registerUser(UserDTO userRegistrationDTO);
}
