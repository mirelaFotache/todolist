package todo.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import todo.exceptions.InvalidParameterException;
import todo.repository.UserRepository;
import todo.repository.models.User;
import todo.service.api.UserService;
import todo.service.dto.UserAdapter;
import todo.service.dto.UserDto;

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

    private UserRepository userRepository;
    //private ProjectRepository projectRepository;

/*    public UserServiceImpl(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }*/
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDto> getUserById(String id) {
        final User user = userRepository.findById(UUID.fromString(id)).get();
        return Optional.of(UserAdapter.toDto(user));
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
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Optional<UserDto> findByAliasAndPassword(String alias, String password) {
        User user = userRepository.findByAliasAndPassword(alias, password);
        return Optional.of(UserAdapter.toDto(user));
    }

    @Override
    public Page<UserDto> getAllUsers() {
        List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<UserDto> collect = users.stream().map(UserAdapter::toDto).collect(Collectors.toList());
        return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by("alias")), collect.size());
    }

    @Override
    @HystrixCommand(fallbackMethod = "getAllUsers_Fallback")
    public Page<UserDto> getAllUsersHystrixTest() {
        List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<UserDto> collect = users.stream().map(UserAdapter::toDto).collect(Collectors.toList());
        try {
            Thread.sleep(60000L);
        }catch (InterruptedException e){
            System.out.println("Network fake delay....");
        }
        return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by("alias")), collect.size());
    }

    @SuppressWarnings("unused")
    public Page<UserDto> getAllUsers_Fallback() {
        User user = new User();
        user.setAlias("default_alias");
        user.setFirstName("default_first_name");
        user.setLastname("default_last_name");
        List<User> users = new ArrayList<>();
        users.add(user);
        List<UserDto> collect = users.stream().map(UserAdapter::toDto).collect(Collectors.toList());
        return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by("alias")), collect.size());
    }

    @Override
    @Transactional()
    public Optional<UserDto> insertUser(UserDto userDto) {
        String isNotValidMsg = isValidUser(userDto.getAlias());
        //Set<ProjectDto> products = userDto.getProjects();
        final User user = UserAdapter.fromDto(userDto);
        // Find projects that exist in the database and add them to user object. Save() method does persist when project does not have
        // id set and update if an project with id was found in the database
 /*       if (products != null)
            products.forEach(project -> {
                Set<Project> projects = new HashSet<>();
                final Project dbProject = projectRepository.findByLabelEquals(project.getLabel());
                if (dbProject != null) {
                    dbProject.setLabel(project.getLabel());
                    dbProject.setUsers(null);
                    projects.add(dbProject);
                } else {
                    final Project projectNew = new Project();
                    projectNew.setLabel(project.getLabel());
                    projects.add(projectNew);
                }
                user.setProjects(projects);
            });*/
        //TODO check and if role already exists not insert it again

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

/*    public ProjectRepository getProjectRepository() {
        return projectRepository;
    }*/
}
