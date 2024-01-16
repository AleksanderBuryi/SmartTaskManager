package by.tms.taskmanager.service;

import by.tms.taskmanager.dto.request.TaskRequestDto;
import by.tms.taskmanager.entity.Status;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.repository.TaskRepository;
import by.tms.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Task create(TaskRequestDto request, User user) {
        Task task = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .status(Status.TODO)
                .user(user)
                .build();

        return taskRepository.save(task);
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepository.findAllByUser(user);
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
