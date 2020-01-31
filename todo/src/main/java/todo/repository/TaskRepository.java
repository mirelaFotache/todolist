package todo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import todo.repository.models.Task;

import java.util.Collection;
import java.util.UUID;

public interface TaskRepository extends CrudRepository<Task, UUID> {

    @Query("select task from Task task where task.description=:name")
    Collection<Task> getTasksByName(@Param("name") String name);
}
