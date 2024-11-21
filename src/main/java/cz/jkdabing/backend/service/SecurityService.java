package cz.jkdabing.backend.service;

import cz.jkdabing.backend.entity.UserEntity;

public interface SecurityService {

    UserEntity getCurrentUser();
}
