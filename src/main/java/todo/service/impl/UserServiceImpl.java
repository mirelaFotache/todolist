package todo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import todo.repository.UserRepository;
import todo.repository.models.User;
import todo.service.api.UserService;
import todo.service.dto.UserAdapter;
import todo.service.dto.UserDto;
import todo.service.exceptions.InvalidParameterException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UserServiceImpl implements UserService {

    public static final String USERDTO_ID_INVALID = "userdto.id.invalid";
    public static final String USERDTO_DUPLICATE_ALIAS = "userdto.duplicate.alias";

    public UserRepository getUserRepository() {
        return userRepository;
    }

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<List<UserDto>> getUsersByName(String firstName, String lastName) {
        return Optional.of(UserAdapter.toDtoSet(new ArrayList<>(userRepository.getUsersByName(firstName, lastName))));
    }

    @Override
    public Optional<UserDto> getUserByAlias(String alias) {
        return Optional.of(UserAdapter.toDto(userRepository.getUserByAlias(alias)));
    }

    @Override
    public Page<UserDto> getAllUsers() {
        List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<UserDto> collect = users.stream().map(UserAdapter::toDto).collect(Collectors.toList());
        return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by("alias")), collect.size());
    }

    @Override
    @Transactional()
    public Optional<UserDto> insertUser(UserDto userDto) {
        String isNotValidMsg = isValidUser(userDto.getAlias());
        if (isNotValidMsg.isEmpty()) {
            return Optional.of(UserAdapter.toDto(userRepository.save(UserAdapter.fromDto(userDto))));
        } else {
            throw new InvalidParameterException(isNotValidMsg);
        }
    }

    @Override
    @Transactional
    public Optional<UserDto> updateUser(String id, UserDto userDto) {
        if (id != null) {
            Optional<User> userOptional = userRepository.findById(UUID.fromString(id));
            if (userOptional.isPresent()) {
                if(userDto.getAlias()!=null) {
                    if (!userDto.getAlias().equals(userOptional.get().getAlias())) {
                        String isNotValidMsg = isValidUser(userDto.getAlias());
                        if (isNotValidMsg.isEmpty()) {
                            throw new InvalidParameterException(isNotValidMsg);
                        }
                    }
                    UserAdapter.fromDtoToUser(userDto, userOptional.get());
                    return Optional.of(UserAdapter.toDto(userRepository.save(userOptional.get())));
                }else{
                    throw new InvalidParameterException("userdto.notempty.alias");
                }
            } else {
                throw new InvalidParameterException(USERDTO_ID_INVALID);
            }
        } else {
            throw new InvalidParameterException(USERDTO_ID_INVALID);
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(String id) {
        if (id != null) {
            Optional<User> userOptional = userRepository.findById(UUID.fromString(id));
            userOptional.ifPresent(user -> userRepository.delete(user));
            return true;
        } else {
            return false;
        }
    }


    public String isValidUser(String alias) {
        String msg = "";
        msg = validateAlias(alias, msg);
        return msg;
    }

    private String validateAlias(String alias, String msg) {
        User user = userRepository.getUserByAlias(alias);
        if (user != null)
            msg = USERDTO_DUPLICATE_ALIAS;
        return msg;
    }
}
