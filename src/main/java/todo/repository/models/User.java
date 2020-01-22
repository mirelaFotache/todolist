package todo.repository.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseModel {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "alias")
    private String alias;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "project_user",
            joinColumns = {@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id_fkey"), referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "project_id_fkey"), referencedColumnName = "id")},
            uniqueConstraints = {@UniqueConstraint(name = "uc_user_project", columnNames = {"user_id", "project_id"})},
            indexes = {@Index(name = "idx_user_project", columnList = "project_id")})
    private Set<Project> projects;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //CascadeType.ALL would cause deletion of role associated to to current user!
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_ROLE"), referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "FK_ROLE"), referencedColumnName = "id"))
    private Set<Role> roles;

    @Version
    private Integer version;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
