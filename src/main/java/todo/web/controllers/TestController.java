package todo.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.config.YAMLToDoConfig;

import javax.transaction.Transactional;
import java.util.Locale;

@RestController
@RequestMapping(value = "/tests")
@Transactional
public class TestController {

    @Autowired
    private YAMLToDoConfig config;

    @Autowired
    MessageSource messageSource;

    /**
     * Test reading application name from YML file
     *
     * @return application name
     */
    @GetMapping(value = "/appName")
    public ResponseEntity<String> getAppName() {
        return ResponseEntity.ok(config.getSettings().get(1).getName());
    }

    /**
     * Test internationalisation
     *
     * @param locale
     * @return application name
     */
    @GetMapping("/appNameI18")
    public ResponseEntity<String> index(Locale locale) {
        return ResponseEntity.ok(messageSource.getMessage("application.name", null, locale));
    }

}
