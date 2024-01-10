package by.tms.taskmanager.controller;

import by.tms.taskmanager.dto.TaskRequestDto;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.service.TaskService;
import by.tms.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequestDto request) {
        return new ResponseEntity<>(taskService.create(request), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<Task> tasks = taskService.getTasksByUser(user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}
