package by.tms.taskmanager.repository;

import by.tms.taskmanager.entity.Step;
import by.tms.taskmanager.entity.StepExecutionTime;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.TimeSpent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimeSpentRepository extends JpaRepository<TimeSpent, Long> {
    Optional<TimeSpent> findTimeSpentByTask(Task task);
    void deleteAllByTaskId(Long id);
}
