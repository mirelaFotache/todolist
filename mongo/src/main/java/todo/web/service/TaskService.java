package todo.web.service;

import org.springframework.stereotype.Service;
import todo.web.dto.TaskDto;

import java.util.List;

@Service
public interface TaskService {

    public List<TaskDto> getTasksByProjectName(String projectName);

    public TaskDto upsertTask(TaskDto taskDto);

    public Boolean deleteTask(String id);

    public List<TaskDto> getTasksByDate(String date);

    public List<TaskDto> getTasksByDateBetween(String dateStrGte, String dateStrLte);

}
