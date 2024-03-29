package by.tms.taskmanager.service;

import by.tms.taskmanager.dto.request.StepRequestDto;
import by.tms.taskmanager.dto.response.StepResponseDto;
import by.tms.taskmanager.entity.Step;
import by.tms.taskmanager.entity.StepExecutionTime;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.mapper.GeneralMapper;
import by.tms.taskmanager.repository.StepExecutionTimeRepository;
import by.tms.taskmanager.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StepService {
    private final StepRepository stepRepository;
    private final StepExecutionTimeRepository stepExecutionTimeRepository;
    private final GeneralMapper generalMapper;

    public StepResponseDto create(StepRequestDto request, Task task) {
        Step step = generalMapper.mapToStep(request);
        step.setTask(task);

        Step savedStep = stepRepository.save(step);

        stepExecutionTimeRepository.save(StepExecutionTime.builder()
                .step(savedStep)
                .modifiedTime(LocalDateTime.now())
                .build());

        return generalMapper.mapToStepResponseDto(savedStep);
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

        return generalMapper.mapToStepResponseDto(fromStep);
    }

    private List<StepResponseDto> getStepsResponseDtos(List<Step> steps) {
        List<StepResponseDto> stepResponseDtoList = new ArrayList<>();
        for (Step step : steps) {
            stepResponseDtoList.add(generalMapper.mapToStepResponseDto(step));
        }
        return stepResponseDtoList;
    }

    @Transactional
    public void deleteStep(Long id) {
        stepRepository.deleteById(id);
    }
}
