package todo.web.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.service.api.ContactDetailsService;
import todo.service.api.UserService;
import todo.service.dto.ContactDetailsDto;
import todo.service.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/contactdetails")
public class ContactDetailsController {

    private ContactDetailsService contactDetailsService;

    public ContactDetailsController(ContactDetailsService contactDetailsService) {
        this.contactDetailsService = contactDetailsService;
    }

    @GetMapping(value = "/{alias}")
    public ResponseEntity<ContactDetailsDto> getContactDetailsByUserName(@PathVariable("alias") String alias) {
        return ResponseEntity.ok(contactDetailsService.getContactDetailsByUserAlias(alias).get());
    }

    @GetMapping
    public ResponseEntity<Page<ContactDetailsDto>> getAllContactDetails() {
        return ResponseEntity.ok(contactDetailsService.getAllContactDetails());
    }

    @PutMapping
    public ResponseEntity<ContactDetailsDto> addContactDetails(@RequestBody @Valid ContactDetailsDto contactDetailsDto) {
        return ResponseEntity.ok(contactDetailsService.insertContactDetails(contactDetailsDto).get());
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<ContactDetailsDto> updateContactDetails(@PathVariable String id, @RequestBody @Valid ContactDetailsDto contactDetailsDto) {
        return ResponseEntity.ok(contactDetailsService.updateContactDetails(id, contactDetailsDto).get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteContactDetails(@PathVariable("id") String id) {
        contactDetailsService.deleteContactDetails(id);
        return ResponseEntity.noContent().build();
    }
}
