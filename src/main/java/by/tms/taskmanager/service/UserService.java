package by.tms.taskmanager.service;

import by.tms.taskmanager.config.JWTTokenProvider;
import by.tms.taskmanager.dto.request.AuthRequestDto;
import by.tms.taskmanager.dto.request.RegistrationRequestDto;
import by.tms.taskmanager.dto.response.AuthResponseDto;
import by.tms.taskmanager.dto.response.UserResponseDto;
import by.tms.taskmanager.entity.Role;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;

    public UserResponseDto register(RegistrationRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new RuntimeException("Email already exists"); //todo throw custom exception

        User userToRegister = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userToRegister.setRoles(Collections.singleton(Role.USER));

        userRepository.save(userToRegister);

        return UserResponseDto.builder()
                .id(userToRegister.getId())
                .name(userToRegister.getName())
                .surname(userToRegister.getSurname())
                .email(userToRegister.getEmail())
                .roles(userToRegister.getRoles())
                .build();
    }

    public AuthResponseDto login(AuthRequestDto request) {
        User user  = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Email not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password"); //todo throw custom exception
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getPassword(),  user.getRoles());

        return AuthResponseDto.builder().token(token).build();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }


    public void update(User user) {
        if (user != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }
}
