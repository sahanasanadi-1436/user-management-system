package UserManagementSystem.controller;

import UserManagementSystem.dto.TaskResponse;
import UserManagementSystem.dto.UserResponse;
import UserManagementSystem.entity.Role;
import UserManagementSystem.entity.Task;
import UserManagementSystem.entity.User;
import UserManagementSystem.exception.ResourceNotFoundException;
import UserManagementSystem.repository.TaskRepository;
import UserManagementSystem.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public UserController(UserRepository userRepository,
            TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    // Get logged-in user's profile
    @GetMapping("/profile")
    public UserResponse getMyProfile(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return convertToUserResponse(user);
    }

    // Get tasks assigned to logged-in user
    @GetMapping("/tasks")
    public List<TaskResponse> getMyTasks(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return taskRepository.findByAssignedUser(user)
                .stream()
                .map(this::convertToTaskResponse)
                .toList();
    }

    private UserResponse convertToUserResponse(User user) {

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

    private TaskResponse convertToTaskResponse(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getAssignedUser().getId(),
                task.getAssignedUser().getName(),
                task.getCreatedBy().getEmail()
        );
    }
}