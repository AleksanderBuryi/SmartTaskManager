package by.tms.taskmanager.service;

import by.tms.taskmanager.dto.TaskRequestDto;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.repository.TaskRepository;
import by.tms.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Task create(TaskRequestDto request) {
        Task task = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        //todo add user
        return taskRepository.save(task);
    }

    public List<Task> getTasksByUser(User user) {
        //todo make getting user task list logic
        return taskRepository.findAllByUser(user);
    }
}
