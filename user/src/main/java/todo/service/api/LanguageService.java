package todo.service.api;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import todo.service.dto.LanguageDto;

import java.util.Optional;

@Service
public interface LanguageService {

    public Optional<LanguageDto> getLanguageByUserName(String alias);

    public Page<LanguageDto> getAllLanguages();

    public Optional<LanguageDto> insertLanguage(LanguageDto languageDto);

    public Optional<LanguageDto> updateLanguage(String id, LanguageDto languageDto);

    public void deleteLanguage(String id);

}
