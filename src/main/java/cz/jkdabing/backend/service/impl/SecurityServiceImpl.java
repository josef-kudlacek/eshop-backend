package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.SecurityService;
import cz.jkdabing.backend.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecurityServiceImpl implements SecurityService {

    private static final String ANONYMOUS_USER = "anonymousUser";

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    public SecurityServiceImpl(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getCurrentUser() {
        String currentUserName = SecurityUtil.getCurrentUserPrincipal();
        if (currentUserName == null || currentUserName.equals(ANONYMOUS_USER)) {
            return null;
        }

        return handleFindUserByUsername(currentUserName);
    }

    @Override
    public void logoutUser() {
        String currentUsername = SecurityUtil.getCurrentUserPrincipal();
        if (currentUsername == null || currentUsername.equals(ANONYMOUS_USER)) {
            return;
        }

        UserEntity user = handleFindUserByUsername(currentUsername);
        if (user != null) {
            user.setTokenVersion(user.getTokenVersion() + 1);
            userRepository.save(user);
        }
    }

    @Override
    public UUID getCustomerId(String token) {
        String extractedToken = SecurityUtil.extractToken(token);
        if (extractedToken == null) {
            return null;
        }
        return UUID.fromString(jwtTokenProvider.getSubjectIdFromToken(extractedToken));
    }

    private UserEntity handleFindUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
