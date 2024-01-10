package by.tms.taskmanager.service;

import by.tms.taskmanager.config.JWTTokenProvider;
import by.tms.taskmanager.dto.AuthRequestDto;
import by.tms.taskmanager.dto.RegistrationRequestDto;
import by.tms.taskmanager.entity.Role;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.entity.UserPrincipal;
import by.tms.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    private String TOKEN;

    public User register(RegistrationRequestDto request) {
        User userToRegister = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.USER)
                .build();

        userRepository.save(userToRegister);

        return userToRegister;
    }

    public String login(AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Optional<User> byEmail = userRepository.findByEmail(request.getEmail());
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            UserPrincipal userPrincipal = UserPrincipal.builder()
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .build();


            TOKEN = jwtTokenProvider.generateToken(user.getEmail(), user.getPassword(),  user.getRole());
        }

        return TOKEN;
    }

    public User getUserById(Long id) {
        return userRepository.getUserById(id).orElse(null);
    }
}
