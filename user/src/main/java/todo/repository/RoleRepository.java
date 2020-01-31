package todo.repository;

import org.springframework.data.repository.CrudRepository;
import todo.repository.models.Role;

import java.util.UUID;

public interface RoleRepository extends CrudRepository<Role, UUID> {
}
