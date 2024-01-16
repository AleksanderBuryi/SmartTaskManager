package by.tms.taskmanager.service;

import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;


    public Optional<User> getUserById(Long id) {
        return adminRepository.getUserById(id);
    }

    public List<User> findAll() {
        return adminRepository.findAll();
    }

    public void remove(User user) {
        adminRepository.delete(user);
    }

    public void removeById(Long id) {
        if (id != null) {
            adminRepository.deleteById(id);
        }
    }

    public void update(User user) {
        if (user != null) {
            adminRepository.save(user);
        }
    }
}
