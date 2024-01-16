package by.tms.taskmanager.controller;

import by.tms.taskmanager.dto.request.UpdateUserRoleDto;
import by.tms.taskmanager.dto.response.TaskResponseDto;
import by.tms.taskmanager.dto.response.UserResponseDto;
import by.tms.taskmanager.entity.Role;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.service.AdminService;
import by.tms.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final TaskService taskService;

    @GetMapping("/{id}")
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
    public ResponseEntity<UserResponseDto> changeUserRole(@PathVariable("id") Long id, @RequestBody UpdateUserRoleDto userDto) {
        Optional<User> existingUser = adminService.getUserById(id);
        if (existingUser.isPresent()) {
            log.info("Updating user role. User with id = " + id + " was found.");
            existingUser.get()
                    .setRoles(Collections.singleton(userDto.getRole().equalsIgnoreCase("ADMIN")
                            ? Role.ADMIN
                            : Role.USER));
            adminService.update(existingUser.get());

            return ResponseEntity.ok(UserResponseDto.builder()
                    .id(existingUser.get().getId())
                    .name(existingUser.get().getName())
                    .surname(existingUser.get().getSurname())
                    .email(existingUser.get().getEmail())
                    .roles(existingUser.get().getRoles())
                    .build());
        } else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        log.info("Delete user = " + id);
        adminService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        log.info("Get all tasks");
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
}
