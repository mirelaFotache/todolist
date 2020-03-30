package todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * EnableMongoAuditing is needed for @CreatedDate and @LastModifiedDate to work and automatically fill in the corresponding fields
 */
@SpringBootApplication
@EnableMongoAuditing
public class SpringMongoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMongoApplication.class, args);
    }
}
