package todo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import todo.repository.models.TaskItems;

import java.util.Collection;
import java.util.UUID;

public interface TaskItemsRepository extends CrudRepository<TaskItems, UUID> {

    @Query("select task from TaskItems task where task.label=:name")
    Collection<TaskItems> getTasksItemsByName(@Param("name") String name);
}
