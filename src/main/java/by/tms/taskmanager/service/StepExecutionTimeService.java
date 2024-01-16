package by.tms.taskmanager.service;

import by.tms.taskmanager.repository.StepExecutionTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StepExecutionTimeService {
    private final StepExecutionTimeRepository stepExecutionTimeRepository;

}
