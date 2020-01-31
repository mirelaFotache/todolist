package todo.service.api;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import todo.service.dto.ContactDetailsDto;

import java.util.Optional;

@Service
public interface ContactDetailsService {

    public Optional<ContactDetailsDto> getContactDetailsByUserAlias(String alias);

    public Page<ContactDetailsDto> getAllContactDetails();

    public Optional<ContactDetailsDto> insertContactDetails(ContactDetailsDto contactDetailsDto);

    public Optional<ContactDetailsDto> updateContactDetails(String id, ContactDetailsDto contactDetailsDto);

    public void deleteContactDetails(String id);
}
