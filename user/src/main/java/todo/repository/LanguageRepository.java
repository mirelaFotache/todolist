package todo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import todo.repository.models.Language;

import java.util.UUID;

public interface LanguageRepository extends CrudRepository<Language, UUID> {

    @Query("select lang from Language lang where lang.user.alias=:alias")
    Language getLanguageByUserName(@Param("alias") String alias);

    @Query("select lang from Language lang where lang.user.id=:id and lang.code=:code")
    Language getLanguageByUserId(@Param("id") UUID id, @Param("code") String code);
}
