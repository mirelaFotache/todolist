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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "project_user",
            joinColumns = {@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id_fkey"), referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "project_id_fkey"), referencedColumnName = "id")},
            uniqueConstraints = {@UniqueConstraint(name = "uc_user_project", columnNames = {"user_id", "project_id"})},
            indexes = {@Index(name = "idx_user_project", columnList = "project_id")})
    private Set<Project> projects;

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



}
