package by.tms.taskmanager.repository;

import by.tms.taskmanager.entity.Step;
import by.tms.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepRepository extends JpaRepository<Step, Long> {
    List<Step> findAllByTask(Task task);
}
