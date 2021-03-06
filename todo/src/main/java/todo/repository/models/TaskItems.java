package todo.repository.models;

import javax.persistence.*;

@Entity
@Table(name = "task_items")
public class TaskItems extends BaseModel {

    private String label;

    private Boolean completed;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Task.class)
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "fk_task_task_items"))
    private Task task;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
