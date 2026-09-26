package UserManagementSystem.repository;

import UserManagementSystem.entity.Task;
import UserManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedUser(User user);

    List<Task> findByCreatedBy(User user);
}