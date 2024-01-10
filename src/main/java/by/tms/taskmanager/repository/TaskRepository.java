package by.tms.taskmanager.repository;

import by.tms.taskmanager.entity.Status;
import by.tms.taskmanager.entity.Task;
import by.tms.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);
    List<Task> findByNameAndUser(String name, User user);

    List<Task> findByStatusAndUser(Status status, User user);

}
