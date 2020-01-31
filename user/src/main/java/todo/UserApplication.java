package todo;

import org.springframework.context.annotation.PropertySource;
import todo.authenticationjwt.CertificateService;
import todo.config.YAMLToDoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import todo.repository.UserRepository;
import todo.repository.models.Role;
import todo.repository.models.User;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(UserApplication.class, args);
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

        getCertificate(context);
    }

    private static void getCertificate(ConfigurableApplicationContext context) {
        CertificateService certificateService = context.getBean(CertificateService.class);
        certificateService.getCertificate();
    }

}

