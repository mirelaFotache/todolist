package todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import todo.repository.UserRepository;
import todo.repository.models.Role;
import todo.repository.models.User;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ToDoApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(ToDoApplication.class, args);
        setDefaultUser(context);
    }

    private static void setDefaultUser(ConfigurableApplicationContext context) {
        UserRepository repository = context.getBean(UserRepository.class);
        User u = repository.getUserByAlias("admin");
        if (u == null) {
            u = new User();
            u.setFirstName("admin");
            u.setLastname("admin");
            u.setAlias("admin");
            u.setPassword("admin");
            u.setActive(Boolean.TRUE);
            Role role = new Role();
            role.setName("ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            u.setRoles(roles);
            repository.save(u);
        }
    }
    //TODO  Spring Profiles
}
