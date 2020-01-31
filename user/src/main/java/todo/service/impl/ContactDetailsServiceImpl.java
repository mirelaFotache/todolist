package todo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import todo.repository.ContactDetailsRepository;
import todo.repository.UserRepository;
import todo.repository.models.ContactDetails;
import todo.repository.models.User;
import todo.service.api.ContactDetailsService;
import todo.service.dto.ContactDetailsAdapter;
import todo.service.dto.ContactDetailsDto;
import todo.exceptions.InvalidParameterException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ContactDetailsServiceImpl implements ContactDetailsService {

    public static final String USERDTO_ID_INVALID = "userdto.id.invalid";
    public static final String CONTACTDETAILS_ID_INVALID = "contactdetailsdto.id.invalid";
    public static final String CONTACTDETAILS_USER_WITH_PHONE_EXISTS = "contactdetails.user.with.phone.exists";
    private ContactDetailsRepository contactDetailsRepository;
    private UserRepository userRepository;

    public ContactDetailsServiceImpl(ContactDetailsRepository contactDetailsRepository, UserRepository userRepository) {
        this.contactDetailsRepository = contactDetailsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<ContactDetailsDto> getContactDetailsByUserAlias(String alias) {
        return Optional.of(ContactDetailsAdapter.toDto(contactDetailsRepository.getContactDetailsByUserName(alias)));
    }

    @Override
    public Page<ContactDetailsDto> getAllContactDetails() {
        List<ContactDetails> contactDetails = StreamSupport.stream(contactDetailsRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<ContactDetailsDto> collect = contactDetails.stream().map(ContactDetailsAdapter::toDto).collect(Collectors.toList());
        return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by("city")), collect.size());
    }

    @Override
    public Optional<ContactDetailsDto> insertContactDetails(ContactDetailsDto contactDetailsDto) {

        final String userid = contactDetailsDto.getUser().getId();
        if (userid != null && !userid.isEmpty()) {
            final ContactDetails contactDetailsByUserId = contactDetailsRepository.getContactDetailsByUserPhone(UUID.fromString(userid), contactDetailsDto.getTelephone());
            if (contactDetailsByUserId != null)
                throw new InvalidParameterException(CONTACTDETAILS_USER_WITH_PHONE_EXISTS);
            Optional<User> userOptional = userRepository.findById(UUID.fromString(userid));
            if (userOptional.isPresent()) {
                final ContactDetails contactDetails = ContactDetailsAdapter.fromDto(contactDetailsDto);
                contactDetails.setUser(userOptional.get());
                return Optional.of(ContactDetailsAdapter.toDto(contactDetailsRepository.save(contactDetails)));
            } else {
                throw new InvalidParameterException(USERDTO_ID_INVALID);
            }
        } else {
            throw new InvalidParameterException(CONTACTDETAILS_ID_INVALID);
        }
    }

    @Override
    public Optional<ContactDetailsDto> updateContactDetails(String id, ContactDetailsDto contactDetailsDto) {
        if (id != null) {
            Optional<User> userOptional = userRepository.findById(UUID.fromString(contactDetailsDto.getUser().getId()));
            if (userOptional.isPresent()) {
                final Optional<ContactDetails> contactDetailsOptional = contactDetailsRepository.findById(UUID.fromString(id));
                if (contactDetailsOptional.isPresent()) {
                    ContactDetails contactDetails = contactDetailsOptional.get();
                    contactDetails.setUser(userOptional.get());
                    ContactDetailsAdapter.fromDtoToContactDetails(contactDetailsDto, contactDetails);
                    return Optional.of(ContactDetailsAdapter.toDto(contactDetailsRepository.save(contactDetails)));
                } else {
                    throw new InvalidParameterException(CONTACTDETAILS_ID_INVALID);
                }
            } else {
                throw new InvalidParameterException(USERDTO_ID_INVALID);
            }
        } else {
            throw new InvalidParameterException(CONTACTDETAILS_ID_INVALID);
        }
    }

    @Override
    public void deleteContactDetails(String id) {
        if (id != null) {
            Optional<ContactDetails> userOptional = contactDetailsRepository.findById(UUID.fromString(id));
            userOptional.ifPresent(contactDetails -> contactDetailsRepository.delete(contactDetails));
        }
    }

}
