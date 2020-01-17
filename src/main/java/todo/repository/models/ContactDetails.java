package todo.repository.models;

import javax.persistence.*;

@Entity
@Table(name = "contact_details")
public class ContactDetails extends BaseModel {

    private String mail;

    private String telephone;

    private String city;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Version
    private Integer version;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
