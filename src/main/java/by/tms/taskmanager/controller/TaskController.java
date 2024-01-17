package by.tms.taskmanager.controller;

import by.tms.taskmanager.dto.request.TaskRequestDto;
import by.tms.taskmanager.dto.response.StepResponseDto;
import by.tms.taskmanager.dto.response.TaskResponseDto;
import by.tms.taskmanager.entity.Status;
import by.tms.taskmanager.entity.Step;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.service.TaskService;
import by.tms.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/{userId}/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable Long userId, @RequestBody TaskRequestDto request) {
        log.info("Create task by user with id = " + userId);
        Optional<User> user = userService.getUserById(userId);
        return user.map(value ->
                new ResponseEntity<>(taskService.create(request, value), HttpStatus.OK)).
                orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTasksByUser(@PathVariable Long userId) {
        log.info("Get tasks by user with id =  " + userId);
        Optional<User> user = userService.getUserById(userId);
        return user.map(value -> new ResponseEntity<>(taskService.getTasksByUser(value), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Get task by id = " + id);
        Optional<Task> existingTask = taskService.findTaskById(id);
        return existingTask.map(task -> ResponseEntity.ok(TaskResponseDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .difficulty(task.getDifficulty())
                .status(task.getStatus())
                .build())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @RequestBody TaskRequestDto request, @PathVariable Long userId) {
        Optional<Task> existingTask = taskService.findTaskById(id);
        if (existingTask.isPresent()) {
            log.info("Update user with id = " + id);

            Task updatedTask = existingTask.get();
            updatedTask.setName(request.getName());
            updatedTask.setDescription(request.getDescription());
            updatedTask.setStatus(Status.INPROGRESS);

            return ResponseEntity.ok(taskService.updateTask(updatedTask));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id, @PathVariable Long userId) {
        Optional<Task> existingTask = taskService.findTaskById(id);
        if (existingTask.isPresent()) {
            log.info("Delete user, because id = " + id + " was found.");
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TaskResponseDto> completeStep(@PathVariable Long id, @PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Task> task = taskService.findTaskById(id);
        if (user.isEmpty() || task.isEmpty()) return ResponseEntity.notFound().build();
        log.info("Complete task with id = " + id);

        Task completedTask = task.get();
        completedTask.setStatus(Status.DONE);

        return ResponseEntity.ok(taskService.updateTask(completedTask));
    }
}
