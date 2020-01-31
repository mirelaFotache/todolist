package todo.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import todo.config.YAMLToDoConfig;

import java.util.Locale;

@Controller
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

    @GetMapping("/welcome")
    public String homePage(Model model) {
        model.addAttribute("message", "message");
        return "welcome";
    }
}