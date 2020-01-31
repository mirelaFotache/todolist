package todo.service.dto;

import todo.repository.models.ContactDetails;

import java.util.UUID;

public class ContactDetailsAdapter {


    public static ContactDetailsDto toDto(ContactDetails contactDetails) {
        ContactDetailsDto dto = new ContactDetailsDto();
        if (contactDetails != null) {
            dto.setId(contactDetails.getId().toString());
            dto.setCity(contactDetails.getCity());
            dto.setMail(contactDetails.getMail());
            dto.setTelephone(contactDetails.getTelephone());
            dto.setUser(UserAdapter.toDto(contactDetails.getUser()));
        }
        return dto;
    }

    public static ContactDetails fromDto(ContactDetailsDto dto) {
        ContactDetails contactDetails = new ContactDetails();
        if (dto != null) {
            if (dto.getId() != null)
                contactDetails.setId(UUID.fromString(dto.getId()));
            contactDetails.setCity(dto.getCity());
            contactDetails.setMail(dto.getMail());
            contactDetails.setTelephone(dto.getTelephone());
            contactDetails.setUser(UserAdapter.fromDto(dto.getUser()));
        }
        return contactDetails;
    }

    public static void fromDtoToContactDetails(ContactDetailsDto dto, ContactDetails contactDetails) {
        if (dto != null) {
            contactDetails.setCity(dto.getCity());
            contactDetails.setMail(dto.getMail());
            contactDetails.setTelephone(dto.getTelephone());
        }
    }
}
