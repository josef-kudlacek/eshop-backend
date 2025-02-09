package cz.jkdabing.backend.service.impl;

import cz.jkdabing.backend.entity.UserEntity;
import cz.jkdabing.backend.repository.UserRepository;
import cz.jkdabing.backend.security.CustomerDetails;
import cz.jkdabing.backend.security.jwt.JwtTokenProvider;
import cz.jkdabing.backend.service.SecurityService;
import cz.jkdabing.backend.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    public SecurityServiceImpl(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getCurrentUser() {
        UserEntity userEntity = SecurityUtil.getCurrentUserPrincipal();
        if (userEntity == null) {
            return null;
        }

        return userEntity;
    }

    @Override
    public void logoutUser() {
        UserEntity userEntity = getCurrentUser();

        if (userEntity != null) {
            userEntity.setTokenVersion(userEntity.getTokenVersion() + 1);
            userRepository.save(userEntity);
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

    @Override
    public UUID getCurrentCustomerId() {
        if (getCurrentUser() == null) {
            CustomerDetails customerDetails = getCurrentCustomerDetails();
            if (customerDetails == null) {
                return null;
            }
            return UUID.fromString(customerDetails.getUsername());
        }

        return getCurrentUser().getCustomer().getCustomerId();
    }

    private CustomerDetails getCurrentCustomerDetails() {
        return SecurityUtil.getCurrentCustomerPrincipal();
    }
}
