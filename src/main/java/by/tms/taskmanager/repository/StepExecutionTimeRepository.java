package by.tms.taskmanager.repository;

import by.tms.taskmanager.entity.Step;
import by.tms.taskmanager.entity.StepExecutionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StepExecutionTimeRepository extends JpaRepository<StepExecutionTime, Long> {
    Optional<StepExecutionTime> findStepExecutionTimeByStep(Step step);
}
