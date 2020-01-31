package todo.service.api;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import todo.service.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    public Optional<UserDto> getUserById(String id);

    public Optional<List<UserDto>> getUsersByName(String firstName, String lastName);

    public Optional<UserDto> getUserByAlias(String alias);

    public Optional<UserDto> findByAliasAndPassword(String alias, String password);

    public Page<UserDto> getAllUsers();

    public Optional<UserDto> insertUser(UserDto userDto);

    public Optional<UserDto> updateUser(String id, UserDto userDto);

    public boolean deleteUser(String id);
}
