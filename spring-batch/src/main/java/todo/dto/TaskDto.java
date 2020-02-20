package todo.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class TaskDto {

    private String id;

    @NotEmpty
    @Size(max = 50)
    private String description;

    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dateCreated;

    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dateUpdate;

    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dueDate;

    @NotEmpty
    @Size(max = 50)
    private String repeatType;

    @NotNull
    private ProjectDto project;

    private List<TaskItemsDto> taskItems;

    private Boolean deleted;

    public TaskDto() {
        //Empty constructor
    }

    public TaskDto(String id, String description, String dueDate, String repeatType, ProjectDto project, List<TaskItemsDto> taskItems) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
        this.repeatType = repeatType;
        this.project = project;
        this.taskItems = taskItems;
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

    public List<TaskItemsDto> getTaskItems() {
        return taskItems;
    }

    public void setTaskItems(List<TaskItemsDto> taskItems) {
        this.taskItems = taskItems;
    }

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id='" + id + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", description='" + description + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", repeatType='" + repeatType + '\'' +
                ", project=" + project +
                '}';
    }
}
