

package com.example.campusrideshare.service;

import com.example.campusrideshare.model.User;
import com.example.campusrideshare.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring Security will call this when someone logs in
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // We use email as username
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Build a Spring Security user object
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                // user.getRole() is like "ROLE_USER" or "ROLE_DRIVER"
                .roles(user.getRole().replace("ROLE_", ""))
                .build();
    }
}
