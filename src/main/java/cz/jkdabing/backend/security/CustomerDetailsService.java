package cz.jkdabing.backend.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {
        return new CustomerDetails(customerId);
    }
}
