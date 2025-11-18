
package com.example.campusrideshare.controller;

import com.example.campusrideshare.model.User;
import com.example.campusrideshare.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET /register -> show form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // POST /register -> handle form submit
    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("user") User formUser,
                                 @RequestParam("roleChoice") String roleChoice,
                                 Model model) {

        // Simple "email already used" check
        if (userRepository.findByEmail(formUser.getEmail()).isPresent()) {
            model.addAttribute("error", "An account with that email already exists.");
            return "auth/register";
        }

        User user = new User();
        user.setFullName(formUser.getFullName());
        user.setEmail(formUser.getEmail());
        user.setPassword(passwordEncoder.encode(formUser.getPassword()));

        // Map selection to a role
        if ("driver".equals(roleChoice)) {
            user.setRole("ROLE_DRIVER");
        } else if ("passenger".equals(roleChoice)) {
            user.setRole("ROLE_PASSENGER");
        } else {
            user.setRole("ROLE_USER");
        }

        userRepository.save(user);

        // After registering, send them to login page
        return "redirect:/login";
    }

    // GET /login -> custom login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }
}
