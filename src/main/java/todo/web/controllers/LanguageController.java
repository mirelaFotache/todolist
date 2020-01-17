package todo.web.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.service.api.LanguageService;
import todo.service.dto.LanguageDto;

import javax.validation.Valid;

@RestController
@RequestMapping("languages")
public class LanguageController {

    private LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping(value = "/{alias}")
    public ResponseEntity<LanguageDto> getContactDetailsByUserName(@PathVariable("alias") String alias) {
        return ResponseEntity.ok(languageService.getLanguageByUserName(alias).get());
    }

    @GetMapping
    public ResponseEntity<Page<LanguageDto>> getAllLanguages() {
        return ResponseEntity.ok(languageService.getAllLanguages());
    }

    @PutMapping
    public ResponseEntity<LanguageDto> addLanguage(@RequestBody @Valid LanguageDto languageDto) {
        return ResponseEntity.ok(languageService.insertLanguage(languageDto).get());
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<LanguageDto> updateLanguage(@PathVariable String id, @RequestBody @Valid LanguageDto languageDto) {
        return ResponseEntity.ok(languageService.updateLanguage(id, languageDto).get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteLanguage(@PathVariable("id") String id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }
}
