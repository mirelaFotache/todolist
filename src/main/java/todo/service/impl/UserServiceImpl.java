package todo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import todo.repository.ProjectRepository;
import todo.repository.UserRepository;
import todo.repository.models.Project;
import todo.repository.models.User;
import todo.service.api.UserService;
import todo.service.dto.ProjectDto;
import todo.service.dto.UserAdapter;
import todo.service.dto.UserDto;
import todo.service.exceptions.InvalidParameterException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class UserServiceImpl implements UserService {

    public static final String USERDTO_ID_INVALID = "userdto.id.invalid";
    public static final String USERDTO_DUPLICATE_ALIAS = "userdto.duplicate.alias";

    private UserRepository userRepository;
    private ProjectRepository projectRepository;

    public UserServiceImpl(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<UserDto> getUserById(String id) {
        return Optional.of(UserAdapter.toDto(userRepository.findById(UUID.fromString(id)).get()));
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
        Set<ProjectDto> products = userDto.getProjects();
        final User user = UserAdapter.fromDto(userDto);
        // Find projects that exist in the database and add them to user object. Save() method does persist when project does not have
        // id set and update if an project with id was found in the database
        if (products != null) {
            Set<Project> projects= new HashSet<>();
            products.forEach(project -> {
                final Project projectByName = projectRepository.getProjectByName(project.getLabel());
                if (projectByName != null) {
                    final UUID id = projectByName.getId();
                    if (id != null) {
                        projectByName.setLabel(project.getLabel());
                        projects.add(projectByName);
                    }
                }else{
                    final Project projectNew = new Project();
                    projectNew.setLabel(project.getLabel());
                    projects.add(projectNew);
                }
            });
            user.setProjects(projects);
        }
        if (isNotValidMsg.isEmpty()) {
            return Optional.of(UserAdapter.toDto(userRepository.save(user)));
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
                if (userDto.getAlias() != null) {
                    if (!userDto.getAlias().equals(userOptional.get().getAlias())) {
                        String isNotValidMsg = isValidUser(userDto.getAlias());
                        if (!isNotValidMsg.isEmpty()) {
                            throw new InvalidParameterException(isNotValidMsg);
                        }
                    }
                    UserAdapter.fromDtoToUser(userDto, userOptional.get());
                    return Optional.of(UserAdapter.toDto(userRepository.save(userOptional.get())));
                } else {
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

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ProjectRepository getProjectRepository() {
        return projectRepository;
    }
}
