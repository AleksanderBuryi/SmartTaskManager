package by.tms.taskmanager.service;

import by.tms.taskmanager.dto.request.TaskRequestDto;
import by.tms.taskmanager.dto.response.TaskResponseDto;
import by.tms.taskmanager.entity.Difficulty;
import by.tms.taskmanager.entity.Status;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.repository.TaskRepository;
import by.tms.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponseDto create(TaskRequestDto request, User user) {
        Task task = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .difficulty(Difficulty.MODERATE)
                .status(Status.TODO)
                .user(user)
                .build();

        taskRepository.save(task);
        return TaskResponseDto.builder()
                .name(task.getName())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .difficulty(task.getDifficulty())
                .status(task.getStatus())
                .build();
    }

    public List<TaskResponseDto> getTasksByUser(User user) {
        List<Task> tasks = taskRepository.findAllByUser(user);
        return getTaskResponseDtos(tasks);
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<TaskResponseDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return getTaskResponseDtos(tasks);
    }

    private List<TaskResponseDto> getTaskResponseDtos(List<Task> tasks) {
        List<TaskResponseDto> taskResponseDtoList = new ArrayList<>();
        for (Task task : tasks) {
            taskResponseDtoList.add(TaskResponseDto.builder()
                    .id(task.getId())
                    .name(task.getName())
                    .description(task.getDescription())
                    .startDate(task.getStartDate())
                    .difficulty(task.getDifficulty())
                    .status(task.getStatus())
                    .build());
        }
        return taskResponseDtoList;
    }

    public TaskResponseDto updateTask(Task task) {
        Task fromTask = taskRepository.save(task);
        return TaskResponseDto.builder()
                .id(fromTask.getId())
                .name(fromTask.getName())
                .description(fromTask.getDescription())
                .startDate(fromTask.getStartDate())
                .difficulty(fromTask.getDifficulty())
                .status(fromTask.getStatus())
                .build();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
