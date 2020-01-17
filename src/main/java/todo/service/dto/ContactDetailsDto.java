package todo.service.dto;

import todo.repository.models.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.UUID;

public class ContactDetailsDto {

    private String id;

    @NotEmpty
    @Size(max = 50)
    private String mail;

    @NotEmpty
    @Size(max = 50)
    private String telephone;

    @NotEmpty
    @Size(max = 50)
    private String city;

    private UserDto user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
