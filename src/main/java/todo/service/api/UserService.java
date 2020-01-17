package todo.service.api;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import todo.service.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    public Optional<List<UserDto>> getUsersByName(String firstName, String lastName);

    public Optional<UserDto> getUserByAlias(String alias);

    public Page<UserDto> getAllUsers();

    public Optional<UserDto> insertUser(UserDto userDto);

    public Optional<UserDto> updateUser(String id, UserDto userDto);

    public void deleteUser(String id);
}
