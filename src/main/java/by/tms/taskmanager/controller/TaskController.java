package by.tms.taskmanager.controller;

import by.tms.taskmanager.dto.request.TaskRequestDto;
import by.tms.taskmanager.entity.Status;
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
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @PostMapping("/{userId}")
    public ResponseEntity<Task> createTask(@PathVariable Long userId, @RequestBody TaskRequestDto request) {
        log.info("Create task by user with id = " + userId);
        Optional<User> user = userService.getUserById(userId);
        return new ResponseEntity<>(taskService.create(request, user.get()), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        log.info("Get tasks by user with id =  " + userId);
        Optional<User> user = userService.getUserById(userId);
        List<Task> tasks = taskService.getTasksByUser(user.get());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("Get all tasks");
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskRequestDto request) {
        Optional<Task> existingTask = taskService.findTaskById(id);
        if (existingTask.isPresent()) {
            log.info("Update user with id = " + id);
            Task updatedTask = Task.builder()
                    .id(id)
                    .name(request.getName())
                    .description(request.getDescription())
                    .startDate(request.getStartDate())
                    .status(Status.INPROGRESS)
                    .build();
            updatedTask = taskService.updateTask(updatedTask);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        Optional<Task> existingTask = taskService.findTaskById(id);
        if (existingTask.isPresent()) {
            log.info("Delete user, because id = " + id + " was found.");
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
