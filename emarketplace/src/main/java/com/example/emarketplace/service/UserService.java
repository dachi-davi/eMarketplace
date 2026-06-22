package com.example.emarketplace.service;

import com.example.emarketplace.dto.*;
import com.example.emarketplace.entity.User;
import com.example.emarketplace.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) { this.userRepository = userRepository; }

    public UserResponse register(RegisterRequest req) {
        // Validation 1: Username (8-20 letters/numbers)
        if (!req.getUsername().matches("^[a-zA-Z0-9]{8,20}$")) {
            throw new RuntimeException("Username must be 8-20 letters/numbers.");
        }
        // Validation 2: Email
        if (!EmailValidator.getInstance().isValid(req.getEmail())) {
            throw new RuntimeException("Invalid email format.");
        }
        // Validation 3: Age > 13
        if (Period.between(req.getBirthday(), LocalDate.now()).getYears() <= 13) {
            throw new RuntimeException("User must be strictly older than 13.");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword()); // Note: In a real app, hash this!
        user.setBirthday(req.getBirthday());

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    public UserResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getIdentifier())
                .orElseGet(() -> userRepository.findByEmail(req.getIdentifier())
                        .orElseThrow(() -> new RuntimeException("User not found")));

        if (!user.getPassword().equals(req.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        return res;
    }
}