package by.tms.taskmanager.service;

import by.tms.taskmanager.dto.request.StepRequestDto;
import by.tms.taskmanager.dto.response.StepResponseDto;
import by.tms.taskmanager.entity.Step;
import by.tms.taskmanager.entity.StepExecutionTime;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.repository.StepExecutionTimeRepository;
import by.tms.taskmanager.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StepService {
    private final StepRepository stepRepository;
    private final StepExecutionTimeRepository stepExecutionTimeRepository;

    public StepResponseDto create(StepRequestDto request, Task task) {
        Step step = Step.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .isCompleted(request.isCompleted())
                .task(task)
                .build();

        Step savedStep = stepRepository.save(step);

        stepExecutionTimeRepository.save(StepExecutionTime.builder()
                .step(savedStep)
                .modifiedTime(LocalDateTime.now())
                .build());

        return StepResponseDto.builder()
                .id(savedStep.getId())
                .name(savedStep.getTitle())
                .description(savedStep.getDescription())
                .isCompleted(savedStep.isCompleted())
                .build();
    }

    public List<StepResponseDto> findStepsByTask(Task task) {
        List<Step> steps = stepRepository.findAllByTask(task);
        return getStepsResponseDtos(steps);
    }
    public Optional<Step> findStepById(Long id) {
        return stepRepository.findById(id);
    }

    public StepResponseDto updateStep(Step step) {
        Step fromStep = stepRepository.save(step);

        StepExecutionTime stepTime = stepExecutionTimeRepository.findStepExecutionTimeByStep(fromStep).get();
        if (step.isCompleted()) {
            stepTime.setFinishTime(LocalDateTime.now());
        } else {
            stepTime.setModifiedTime(LocalDateTime.now());
        }
        stepExecutionTimeRepository.save(stepTime);

        return StepResponseDto.builder()
                .id(fromStep.getId())
                .name(fromStep.getTitle())
                .description(fromStep.getDescription())
                .isCompleted(fromStep.isCompleted())
                .build();
    }

    private List<StepResponseDto> getStepsResponseDtos(List<Step> steps) {
        List<StepResponseDto> stepResponseDtoList = new ArrayList<>();
        for (Step step : steps) {
            stepResponseDtoList.add(StepResponseDto.builder()
                    .id(step.getId())
                    .name(step.getTitle())
                    .description(step.getDescription())
                    .isCompleted(step.isCompleted())
                    .build());
        }
        return stepResponseDtoList;
    }

    public void deleteStep(Long id) {
        stepRepository.deleteById(id);
    }
}
