package todo.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import todo.data.model.Task;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findTaskByProjectName(String projectName);

    @Query("{ 'dueDate' : { $gte: ?0, $lte: ?1 } }")
    List<Task> findTaskByDueDate(Date dateGte, Date dateLte);
}
