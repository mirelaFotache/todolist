package todo.repository.models;

import java.util.ArrayList;
import java.util.List;

public class Project extends BaseModel {

    private String label;

    private List<Task> tasks = new ArrayList<>();

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

}
