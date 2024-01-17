package by.tms.taskmanager.service;

import by.tms.taskmanager.dto.request.TaskRequestDto;
import by.tms.taskmanager.dto.response.TaskResponseDto;
import by.tms.taskmanager.entity.*;
import by.tms.taskmanager.repository.TaskRepository;
import by.tms.taskmanager.repository.TimeSpentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TimeSpentRepository timeSpentRepository;

    public TaskResponseDto create(TaskRequestDto request, User user) {
        Task task = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .difficulty(Difficulty.MODERATE)
                .status(Status.TODO)
                .user(user)
                .build();

        task = taskRepository.save(task);

        TimeSpent timeSpent = TimeSpent.builder()
                .startTime(LocalDate.now())
                .task(task)
                .build();
        timeSpentRepository.save(timeSpent);

        return TaskResponseDto.builder()
                .id(task.getId())
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
            TimeSpent ts = timeSpentRepository.findTimeSpentByTask(task).get();
            taskResponseDtoList.add(TaskResponseDto.builder()
                    .id(task.getId())
                    .name(task.getName())
                    .description(task.getDescription())
                    .startDate(task.getStartDate())
                    .finishDate(ts.getEndTime())
                    .difficulty(task.getDifficulty())
                    .status(task.getStatus())
                    .build());
        }
        return taskResponseDtoList;
    }

    public TaskResponseDto updateTask(Task task) {
        Task fromTask = taskRepository.save(task);

        TimeSpent timeSpent = timeSpentRepository.findTimeSpentByTask(fromTask).get();
        if (fromTask.getStatus() == Status.DONE) {
            timeSpent.setEndTime(LocalDate.now());
            timeSpentRepository.save(timeSpent);
        }

        return TaskResponseDto.builder()
                .id(fromTask.getId())
                .name(fromTask.getName())
                .description(fromTask.getDescription())
                .startDate(fromTask.getStartDate())
                .finishDate(timeSpent.getEndTime())
                .difficulty(fromTask.getDifficulty())
                .status(fromTask.getStatus())
                .build();
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
