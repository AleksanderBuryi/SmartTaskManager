package by.tms.taskmanager.service;

import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task create(Task task) {
        //todo make task creating logic
        return taskRepository.save(task);
    }

    public List<Task> getTasksByUser(User user) {
        //todo make getting user task list logic
        return taskRepository.findByUser(user);
    }
}
