package todo.repository;

import todo.repository.models.User;

import java.util.Collection;

public interface CustomRepo {

    Collection<User> findAll(BaseFilter filter);

}
