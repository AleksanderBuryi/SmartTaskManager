package by.tms.taskmanager.service;

import by.tms.taskmanager.repository.TimeSpentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeSpentService {
    private final TimeSpentRepository timeSpentRepository;
}
