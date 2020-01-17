package todo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import todo.repository.models.ContactDetails;

import java.util.UUID;

public interface ContactDetailsRepository extends CrudRepository<ContactDetails, UUID> {

    @Query("select details from ContactDetails details where details.user.alias=:alias")
    ContactDetails getContactDetailsByUserName(@Param("alias") String alias);

    @Query("select cd from ContactDetails cd where cd.user.id=:id and cd.telephone=:telephone")
    ContactDetails getContactDetailsByUserPhone(@Param("id") UUID id, @Param("telephone") String telephone);
}
