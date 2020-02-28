package todo.repository.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name="task")
public class Task extends BaseModel {

    private String description;

    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Type(type = "timestamp")
    private Date dueDate;

    @Column(name = "repeat_type")
    private String repeatType;

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne(targetEntity = Project.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_task_project"))
    private Project project;

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "task", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TaskItems> taskItems;

    @Version
    private Integer version;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<TaskItems> getTaskItems() {
        return taskItems;
    }

    public void setTaskItems(List<TaskItems> taskItems) {
        this.taskItems = taskItems;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
