package com.example.campusrideshare.api;

import com.example.campusrideshare.dto.UserDTO;
import com.example.campusrideshare.model.User;
import com.example.campusrideshare.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRestController(UserRepository userRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER USER VIA API
    @PostMapping("/register")
    public UserDTO register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        return toDTO(saved);
    }

    // UPDATE USER INFO
    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody User updated) {
        User user = userRepository.findById(id).orElseThrow();

        user.setFullName(updated.getFullName());
        user.setEmail(updated.getEmail());
        if (updated.getPassword() != null)
            user.setPassword(passwordEncoder.encode(updated.getPassword()));

        User saved = userRepository.save(user);

        return toDTO(saved);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
