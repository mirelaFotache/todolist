package todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import todo.authenticationjwt.CertificateService;

@SpringBootApplication
public class CommonsApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(CommonsApplication.class, args);
        getCertificate(context);
    }

    private static void getCertificate(ConfigurableApplicationContext context) {
        CertificateService certificateService = context.getBean(CertificateService.class);
        certificateService.getCertificate();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


