package todo.service.dto;

import todo.repository.models.Language;

public class LanguageAdapter {

    public static LanguageDto toDto(Language language) {
        LanguageDto dto = new LanguageDto();
        if (language != null) {
            dto.setId(language.getId());
            dto.setCode(language.getCode());
            dto.setLabel(language.getLabel());
            dto.setUser(UserAdapter.toDto(language.getUser()));
        }
        return dto;
    }

    public static Language fromDto(LanguageDto dto) {
        Language language = new Language();
        if (dto != null) {
            language.setCode(dto.getCode());
            language.setLabel(dto.getLabel());
        }
        return language;
    }

    public static void fromDtoToLanguage(LanguageDto dto, Language language) {
        if (dto != null) {
            language.setCode(dto.getCode());
            language.setLabel(dto.getLabel());
        }
    }
}
