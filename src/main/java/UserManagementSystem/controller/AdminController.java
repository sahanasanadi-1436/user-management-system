package UserManagementSystem.controller;

import UserManagementSystem.dto.CreateUserRequest;
import UserManagementSystem.dto.UserResponse;
import UserManagementSystem.entity.Role;
import UserManagementSystem.entity.User;
import UserManagementSystem.exception.DuplicateEmailException;
import UserManagementSystem.exception.ResourceNotFoundException;
import UserManagementSystem.repository.RoleRepository;
import UserManagementSystem.repository.UserRepository;
import UserManagementSystem.dto.UpdateUserRequest;
import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

  
    @GetMapping("/users/{id}")
    public UserResponse getUserById(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id));

        return convertToResponse(user);
    }

   
    @PostMapping("/users")
    public UserResponse createUser(
            @Valid @RequestBody CreateUserRequest request) {


        if (userRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new DuplicateEmailException("Email already exists");
        }

    
        String roleName = request.getRole();

        if (roleName == null || roleName.isBlank()) {
            roleName = "USER";
        }

        String finalRoleName = roleName.toUpperCase();

   
        Role role = roleRepository.findByName(finalRoleName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found: " + finalRoleName));

  
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

       
        user.getRoles().add(role);

     
        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {

        if (!userRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "User not found with id: " + id);
        }

        userRepository.deleteById(id);

        return "User deleted successfully";
    }
    @PutMapping("/users/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id));

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new DuplicateEmailException("Email already exists");
        }
        
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        if (request.getRole() != null && !request.getRole().isBlank()) {

            String roleName = request.getRole().toUpperCase();

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Role not found: " + roleName));

            user.getRoles().clear();
            user.getRoles().add(role);
        }

        User updatedUser = userRepository.save(user);

        return convertToResponse(updatedUser);
    }
    
    private UserResponse convertToResponse(User user) {

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roles
        );
    }
}