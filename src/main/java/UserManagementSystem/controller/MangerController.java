package UserManagementSystem.controller;

import UserManagementSystem.dto.CreateTaskRequest;
import UserManagementSystem.dto.TaskResponse;
import UserManagementSystem.dto.UserResponse;
import UserManagementSystem.entity.Task;
import UserManagementSystem.entity.User;
import UserManagementSystem.entity.Role;
import UserManagementSystem.exception.ResourceNotFoundException;
import UserManagementSystem.repository.TaskRepository;
import UserManagementSystem.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class MangerController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public MangerController(
            UserRepository userRepository,
            TaskRepository taskRepository) {

        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToUserResponse)
                .toList();
    }


    @PostMapping("/tasks")
    public TaskResponse createTask(
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication) {

        User manager = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Manager not found"));

        User assignedUser = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + request.getUserId()));

        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setAssignedUser(assignedUser);
        task.setCreatedBy(manager);

        Task savedTask = taskRepository.save(task);

        return convertToTaskResponse(savedTask);
    }

    
    @GetMapping("/tasks")
    public List<TaskResponse> getManagerTasks(
            Authentication authentication) {

        User manager = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Manager not found"));

        return taskRepository.findByCreatedBy(manager)
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