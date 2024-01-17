package by.tms.taskmanager.controller;

import by.tms.taskmanager.dto.request.TaskRequestDto;
import by.tms.taskmanager.dto.response.TaskResponseDto;
import by.tms.taskmanager.entity.Status;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.mapper.GeneralMapper;
import by.tms.taskmanager.service.TaskService;
import by.tms.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Task commands", description = "CRUD operations on tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final GeneralMapper generalMapper;

    @PostMapping
    @Operation(summary = "Create task for user")
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable Long userId, @RequestBody TaskRequestDto request) {
        log.info("Create task by user with id = " + userId);
        Optional<User> user = userService.getUserById(userId);
        return user.map(value ->
                new ResponseEntity<>(taskService.create(request, value), HttpStatus.OK)).
                orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all tasks of user")
    public ResponseEntity<List<TaskResponseDto>> getTasksByUser(@PathVariable Long userId) {
        log.info("Get tasks by user with id =  " + userId);
        Optional<User> user = userService.getUserById(userId);
        return user.map(value -> new ResponseEntity<>(taskService.getTasksByUser(value), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Get task by id = " + id);
        Optional<Task> existingTask = taskService.findTaskById(id);
        return existingTask.map(task -> ResponseEntity.ok(generalMapper.mapToTaskResponseDto(task)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit task info")
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
    @Operation(summary = "Delete task")
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
    @Operation(summary = "Complete task")
    public ResponseEntity<TaskResponseDto> completeTask(@PathVariable Long id, @PathVariable Long userId) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Task> task = taskService.findTaskById(id);
        if (user.isEmpty() || task.isEmpty()) return ResponseEntity.notFound().build();
        log.info("Complete task with id = " + id);

        Task completedTask = task.get();
        completedTask.setStatus(Status.DONE);

        return ResponseEntity.ok(taskService.updateTask(completedTask));
    }
}
