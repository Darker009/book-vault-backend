package org.tech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.tech.config.JwtUtil;
import org.tech.dto.LoginRequest;
import org.tech.dto.RegisterRequest;
import org.tech.dto.UpdateProfileRequest;
import org.tech.dto.UserResponse;
import org.tech.entity.User;
import org.tech.entity.UserProfile;
import org.tech.repository.UserProfileRepository;
import org.tech.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    private final String ADMIN_EMAIL = "jyotsnashelare5@gmail.com";

    public String registerUser(RegisterRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered!");
        }

        String role = email.equalsIgnoreCase(ADMIN_EMAIL) ? "ROLE_ADMIN" : "ROLE_STUDENT";
        String encodedPassword = encoder.encode(password);

        User user = new User(email, encodedPassword, role);
        user.setProfileUpdated(false);

        User savedUser = userRepository.save(user);

        UserProfile profile = new UserProfile("Default Name", "Default Department", "Default Section", savedUser);
        userProfileRepository.save(profile);

        savedUser.setUserProfile(profile);
        userRepository.save(savedUser);

        return "User registered successfully as " + user.getRole();
    }

    public Map<String, Object> loginUser(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password!"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password!");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();

        String token = jwtUtil.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);

        return response;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    public String updateProfile(UpdateProfileRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            throw new RuntimeException("User profile not found!");
        }

        profile.setName(request.getName());
        profile.setDepartment(request.getDepartment());
        profile.setSection(request.getSection());

        user.setProfileUpdated(true);

        userRepository.save(user);
        userProfileRepository.save(profile);

        return "Profile updated successfully!";
    }

    public List<UserResponse> getAllStudents() {
        List<User> students = userRepository.findByRole("ROLE_STUDENT");
        System.out.println("Fetched students: " + students);
        return students.stream().map(user -> new UserResponse(
                user.getId(), user.getEmail(), user.getRole(), user.isProfileUpdated(), user.getUserProfile()
        )).collect(Collectors.toList());
    }

    public long getTotalStudentCount() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().equals("STUDENT"))
                .count();
    }



}
