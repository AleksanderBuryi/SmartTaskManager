package by.tms.taskmanager.controller;

import by.tms.taskmanager.dto.request.AuthRequestDto;
import by.tms.taskmanager.dto.request.RegistrationRequestDto;
import by.tms.taskmanager.dto.request.UpdateUserDto;
import by.tms.taskmanager.dto.response.AuthResponseDto;
import by.tms.taskmanager.dto.response.UserResponseDto;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User commands", description = "Registration and authentication user, changing user data")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Registration of user")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegistrationRequestDto request) {
        log.info("Creating new user: {}", request);
        return new ResponseEntity<>(userService.register(request), HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(summary = "Authentication of user")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        log.info("Find user in database with email: " + request.getEmail());
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Changing of user data")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserDto userDto) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            log.info("Update user, because id = " + id + " was found.");
            existingUser.get().setName(userDto.getName());
            existingUser.get().setSurname(userDto.getSurname());
            existingUser.get().setEmail(userDto.getEmail());
            existingUser.get().setPassword(userDto.getPassword());

            userService.update(existingUser.get());
            return ResponseEntity.ok(UserResponseDto.builder()
                    .id(existingUser.get().getId())
                    .name(existingUser.get().getName())
                    .surname(existingUser.get().getSurname())
                    .email(existingUser.get().getEmail())
                    .roles(existingUser.get().getRoles())
                    .build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
