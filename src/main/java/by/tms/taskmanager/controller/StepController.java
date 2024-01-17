package by.tms.taskmanager.controller;

import by.tms.taskmanager.dto.request.StepRequestDto;
import by.tms.taskmanager.dto.response.StepResponseDto;
import by.tms.taskmanager.entity.Step;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import by.tms.taskmanager.service.StepService;
import by.tms.taskmanager.service.TaskService;
import by.tms.taskmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user/{userId}/tasks/{taskId}/steps")
@RequiredArgsConstructor
@Slf4j
public class StepController {
    private final StepService stepService;
    private final TaskService taskService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<StepResponseDto> createSteps(@PathVariable Long taskId, @PathVariable Long userId,
                                                       @RequestBody StepRequestDto request) {
        log.info("Create steps by task with id = " + taskId + "by user with id = " + userId);
        Optional<User> user = userService.getUserById(userId);
        Optional<Task> task = taskService.findTaskById(taskId);
        if (user.isEmpty() || task.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(stepService.create(request, task.get()));
    }

    @GetMapping
    public ResponseEntity<List<StepResponseDto>> getStepsByTask(@PathVariable Long userId, @PathVariable Long taskId) {
        log.info("Get Steps by task with id =  " + taskId);
        Optional<User> user = userService.getUserById(userId);
        Optional<Task> task = taskService.findTaskById(taskId);
        if (user.isEmpty() || task.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(stepService.findStepsByTask(task.get()));
    }

    @PutMapping("/{stepId}")
    public ResponseEntity<StepResponseDto> updateStep(@PathVariable Long stepId,
                                                      @PathVariable Long taskId,
                                                      @PathVariable Long userId,
                                                      @RequestBody StepRequestDto request) {
        log.info("Update step with id = " + stepId);

        if (checkPath(stepId, taskId, userId)) return ResponseEntity.notFound().build();

        Step updatedStep = stepService.findStepById(stepId).get();
        updatedStep.setTitle(request.getTitle());
        updatedStep.setDescription(request.getDescription());
        updatedStep.setCompleted(request.isCompleted());

        return ResponseEntity.ok(stepService.updateStep(updatedStep));
    }

    @DeleteMapping("/{stepId}")
    public ResponseEntity<Void> deleteStep(@PathVariable Long stepId,
                                           @PathVariable Long taskId,
                                           @PathVariable Long userId) {
        if (checkPath(stepId, taskId, userId)) return ResponseEntity.notFound().build();

        log.info("Delete step, because id = " + stepId + " was found.");
        stepService.deleteStep(stepId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{stepId}/complete")
    public ResponseEntity<StepResponseDto> completeStep(@PathVariable Long stepId,
                                             @PathVariable Long taskId,
                                             @PathVariable Long userId) {

        if (checkPath(stepId, taskId, userId)) return ResponseEntity.notFound().build();
        log.info("Complete Step with id = " + stepId);

        Step step = stepService.findStepById(stepId).get();
        step.setCompleted(true);

        return ResponseEntity.ok(stepService.updateStep(step));
    }


    private boolean checkPath(Long stepId, Long taskId, Long userId) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Task> task = taskService.findTaskById(taskId);
        Optional<Step> step = stepService.findStepById(stepId);

        return user.isEmpty() || task.isEmpty() || step.isEmpty();
    }
}
