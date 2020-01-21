package todo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import todo.repository.models.Project;
import todo.repository.models.User;

import java.util.Collection;
import java.util.UUID;

public interface ProjectRepository extends CrudRepository<Project, UUID> {

    @Query("select project from Project project where project.label=:name")
    Project getProjectByName(@Param("name") String name);

    Project findByLabelEquals(String label);
}
