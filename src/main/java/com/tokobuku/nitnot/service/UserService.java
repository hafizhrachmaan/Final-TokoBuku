package com.tokobuku.nitnot.service;

import com.tokobuku.nitnot.dto.UserRegistrationRequest;
import com.tokobuku.nitnot.model.Role;
import com.tokobuku.nitnot.model.User;
import com.tokobuku.nitnot.model.UserStatus;
import com.tokobuku.nitnot.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user based on the web registration form.
     * @param request The DTO containing all registration data.
     * @return The newly created user.
     * @throws IllegalStateException if username already exists.
     */
    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());
        newUser.setStatus(UserStatus.PENDING);
        newUser.setFullName(request.getFullName());
        newUser.setEmail(request.getEmail());
        newUser.setPhone(request.getPhone());
        newUser.setAddress(request.getAddress());
        newUser.setLastEducation(request.getLastEducation());
        newUser.setWorkExperience(request.getWorkExperience());
        
        return userRepository.save(newUser);
    }

    /**
     * Adds a new employee, typically by an HR user.
     * The employee is automatically verified.
     * @param username The username for the new employee.
     * @param password The password for the new employee.
     * @param role The role of the new employee.
     * @return The newly created user.
     * @throws IllegalStateException if username already exists.
     */
    public User addEmployee(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(role);
        newUser.setStatus(UserStatus.VERIFIED);
        return userRepository.save(newUser);
    }

    /**
     * Accepts a pending user application.
     * @param userId The ID of the user to accept.
     * @return The updated user, or empty if not found.
     */
    public Optional<User> acceptUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setStatus(UserStatus.VERIFIED);
            return userRepository.save(user);
        });
    }

    /**
     * Rejects a pending user application by deleting the user record.
     * @param userId The ID of the user to reject.
     */
    public void rejectUser(Long userId) {
        userRepository.findById(userId).ifPresent(userRepository::delete);
    }
    
    /**
     * Cuts or terminates an existing employee by deactivating their account (soft delete).
     * @param userId The ID of the user to cut.
     */
    public void cutEmployee(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setStatus(UserStatus.INACTIVE);
            userRepository.save(user);
        });
    }

    /**
     * Retrieves all employees with PENDING status.
     * @return A list of pending users.
     */
    public List<User> getPendingEmployees() {
        return userRepository.findAllByStatus(UserStatus.PENDING);
    }

    /**
     * Retrieves all employees with VERIFIED status.
     * @return A list of verified users.
     */
    public List<User> getVerifiedEmployees() {
        return userRepository.findAllByStatus(UserStatus.VERIFIED);
    }
}
