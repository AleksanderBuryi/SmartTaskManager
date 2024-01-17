package by.tms.taskmanager.controller;

import by.tms.taskmanager.dto.request.UpdateUserRoleDto;
import by.tms.taskmanager.dto.response.TaskResponseDto;
import by.tms.taskmanager.dto.response.UserResponseDto;
import by.tms.taskmanager.entity.Role;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.service.AdminService;
import by.tms.taskmanager.service.TaskService;
import by.tms.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin commands", description = "Manipulate user info")
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;
    private final TaskService taskService;

    @GetMapping("/{id}")
    @Operation(summary = "Find user by id")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
        log.info("Get user with id = " + id);
        Optional<User> user = adminService.getUserById(id);
        return user.map(value -> ResponseEntity.ok(UserResponseDto.builder()
                .id(value.getId())
                .name(value.getName())
                .surname(value.getSurname())
                .email(value.getEmail())
                .roles(value.getRoles())
                .build())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Find all users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("Getting all users");
        List<User> users = adminService.findAll();
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for (User user : users) {
            userResponseDtoList.add(UserResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .email(user.getEmail())
                    .roles(user.getRoles())
                    .build());
        }
        return ResponseEntity.ok(userResponseDtoList);
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Add role to user (used to grant administrator rights to other users)")
    public ResponseEntity<UserResponseDto> changeUserRole(@PathVariable("id") Long id, @RequestBody UpdateUserRoleDto userDto) {
        Optional<User> existingUser = adminService.getUserById(id);
        if (existingUser.isPresent()) {
            log.info("Updating user role. User with id = " + id + " was found.");
            User user = existingUser.get();
            if (userDto.getRole().equalsIgnoreCase("ADMIN")) {
                user.getRoles().add(Role.ADMIN);
            }
            adminService.update(user);

            return ResponseEntity.ok(UserResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .email(user.getEmail())
                    .roles(user.getRoles())
                    .build());
        } else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        log.info("Delete user = " + id);
        adminService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks")
    @Operation(summary = "Get all tasks")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        log.info("Get all tasks");
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
}
