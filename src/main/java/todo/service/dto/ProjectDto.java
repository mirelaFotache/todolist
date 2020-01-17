package todo.service.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class ProjectDto {

    private String id;

    @NotEmpty
    @Size(max=50)
    private String label;

    private List<TaskDto> tasks;

    private List<UserDto> users;

    public ProjectDto() {
        //Empty constructor
    }

    public ProjectDto(String id, String label, List<TaskDto> tasks,List<UserDto> users) {
        this.id = id;
        this.label = label;
        this.tasks = tasks;
        this.users = users;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
