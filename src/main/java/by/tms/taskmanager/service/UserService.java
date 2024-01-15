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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;

    private String TOKEN;

    public User register(RegistrationRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new RuntimeException("Email already exists"); //todo throw custom exception

        User userToRegister = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userToRegister.getRoles().add(Role.USER);

        userRepository.save(userToRegister);

        return userToRegister;
    }

    public String login(AuthRequestDto request) {
        User user  = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Email not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password"); //todo throw custom exception
        }

        TOKEN = jwtTokenProvider.generateToken(user.getEmail(), user.getPassword(),  user.getRoles());

        return TOKEN;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void remove(User user) {
        userRepository.delete(user);
    }

    public void removeById(Long id) {
        if (id != null) {
            userRepository.deleteById(id);
        }
    }

    public void update(User user) {
        if (user != null) {
            userRepository.save(user);
        }
    }
}
