package todo.web.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TaskDto {

    private String id;

    @NotEmpty
    @Size(max = 50)
    private String description;

    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dueDate;

    @NotEmpty
    @Size(max = 50)
    private String repeatType;

    private Boolean done;

    private String projectName;

    public TaskDto() {
        //Empty constructor
    }

    public TaskDto(String id, String description, String dueDate, String repeatType, Boolean done, String projectName) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
        this.repeatType = repeatType;
        this.done = done;
        this.projectName = projectName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
