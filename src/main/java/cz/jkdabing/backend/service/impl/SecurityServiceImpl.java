package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.service.SecurityService;
import cz.jkdabing.backend.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final String ANONYMOUS_USER = "anonymousUser";

    private final UserRepository userRepository;

    public SecurityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getCurrentUser() {
        String currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null || currentUserId.equals(ANONYMOUS_USER)) {
            return null;
        }

        return userRepository.getReferenceById(UUID.fromString(currentUserId));
    }
}
