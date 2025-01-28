package cz.jkdabing.backend.service;

import cz.jkdabing.backend.entity.UserEntity;

import java.util.UUID;

public interface SecurityService {

    UserEntity getCurrentUser();

    void logoutUser();

    UUID getCustomerId(String token);
}
