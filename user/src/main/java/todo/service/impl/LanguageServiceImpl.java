package todo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import todo.repository.LanguageRepository;
import todo.repository.UserRepository;
import todo.repository.models.Language;
import todo.repository.models.User;
import todo.service.api.LanguageService;
import todo.service.dto.LanguageAdapter;
import todo.service.dto.LanguageDto;
import todo.exceptions.InvalidParameterException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class LanguageServiceImpl implements LanguageService {

    public static final String USERDTO_ID_INVALID = "userdto.id.invalid";
    public static final String LANGUAGE_ID_INVALID = "languagedto.id.invalid";
    public static final String LANGUAGEDTO_USER_WITH_LANG_EXISTS = "languagedto.user.with.lang.exists";

    private LanguageRepository languageRepository;
    private UserRepository userRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository, UserRepository userRepository) {
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<LanguageDto> getLanguageByUserName(String alias) {
        return Optional.of(LanguageAdapter.toDto(languageRepository.getLanguageByUserName(alias)));
    }

    @Override
    public Page<LanguageDto> getAllLanguages() {
        List<Language> languages = StreamSupport.stream(languageRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<LanguageDto> collect = languages.stream().map(LanguageAdapter::toDto).collect(Collectors.toList());
        return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by("code")), collect.size());
    }

    @Override
    @Transactional
    public Optional<LanguageDto> insertLanguage(LanguageDto languageDto) {
        final String userId = languageDto.getUser().getId();
        if (userId != null && !userId.isEmpty()) {
            final Language languageByUserId = languageRepository.getLanguageByUserId(UUID.fromString(userId), languageDto.getCode());
            if (languageByUserId != null)
                throw new InvalidParameterException(LANGUAGEDTO_USER_WITH_LANG_EXISTS);
            Optional<User> userOptional = userRepository.findById(UUID.fromString(userId));
            if (userOptional.isPresent()) {
                Language language = LanguageAdapter.fromDto(languageDto);
                language.setUser(userOptional.get());
                return Optional.of(LanguageAdapter.toDto(languageRepository.save(language)));
            } else {
                throw new InvalidParameterException(USERDTO_ID_INVALID);
            }
        } else {
            throw new InvalidParameterException(USERDTO_ID_INVALID);
        }
    }

    @Override
    @Transactional
    public Optional<LanguageDto> updateLanguage(String id, LanguageDto languageDto) {
        if (id != null && !id.isEmpty()) {
            if (languageDto.getUser().getId() != null && !languageDto.getUser().getId().isEmpty()) {
                Optional<User> userOptional = userRepository.findById(UUID.fromString(languageDto.getUser().getId()));
                if (userOptional.isPresent()) {
                    final Optional<Language> languageOptional = languageRepository.findById(UUID.fromString(id));
                    if (languageOptional.isPresent()) {
                        Language language = languageOptional.get();
                        LanguageAdapter.fromDtoToLanguage(languageDto, language);
                        language.setUser(userOptional.get());
                        return Optional.of(LanguageAdapter.toDto(languageRepository.save(language)));
                    } else {
                        throw new InvalidParameterException(LANGUAGE_ID_INVALID);
                    }
                } else {
                    throw new InvalidParameterException(USERDTO_ID_INVALID);
                }
            } else {
                throw new InvalidParameterException(USERDTO_ID_INVALID);
            }
        } else {
            throw new InvalidParameterException("");
        }
    }

    @Override
    @Transactional
    public void deleteLanguage(String id) {
        if (id != null) {
            Optional<Language> languageOptional = languageRepository.findById(UUID.fromString(id));
            languageOptional.ifPresent(user -> languageRepository.delete(languageOptional.get()));
        }
    }
}
