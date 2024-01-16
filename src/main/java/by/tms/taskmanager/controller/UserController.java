package by.tms.taskmanager.controller;

import by.tms.taskmanager.dto.request.AuthRequestDto;
import by.tms.taskmanager.dto.request.RegistrationRequestDto;
import by.tms.taskmanager.dto.request.UpdateUserDto;
import by.tms.taskmanager.dto.response.AuthResponseDto;
import by.tms.taskmanager.dto.response.UserResponseDto;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.service.UserService;
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
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegistrationRequestDto request) {
        log.info("Creating new user: {}", request);
        return new ResponseEntity<>(userService.register(request), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        log.info("Find user in database with email: " + request.getEmail());
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserDto userDto) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            log.info("Update user, because id = " + id + " was found.");
            User user = User.builder()
                    .id(id)
                    .name(userDto.getName())
                    .surname(userDto.getSurname())
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .build();

            userService.update(user);
            return ResponseEntity.ok(UserResponseDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .email(user.getEmail())
                    .roles(existingUser.get().getRoles())
                    .build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
