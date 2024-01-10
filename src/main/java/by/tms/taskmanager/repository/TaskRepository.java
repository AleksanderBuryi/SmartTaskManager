package by.tms.taskmanager.repository;

import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);

}
