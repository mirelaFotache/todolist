package todo.repository.models;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "project")
public class Project extends BaseModel {

    private String label;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

    @ManyToMany(mappedBy = "projects")
    private Set<User> users;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
