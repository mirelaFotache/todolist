package todo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import todo.repository.models.Task;
import todo.repository.models.User;

import java.util.Collection;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    @Query("select user from User user where user.firstName=:firstName and user.lastname=:lastName")
    Collection<User> getUsersByName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("select user from User user where user.alias=:alias")
    User getUserByAlias(@Param("alias") String alias);

    User findByAliasAndPassword(String alias, String password);
}
