package todo.service.api;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import todo.service.dto.TaskDto;

import java.util.Optional;

@Service
public interface TaskService {

    public Page<TaskDto> getTaskByName(String name);

    public Page<TaskDto> getAllTasks();

    public Optional<TaskDto> insertTask(TaskDto taskDto);

    public Optional<TaskDto> updateTask(String id, TaskDto taskDto);

    public void deleteTask(String id);

    public void batchInsert();
}
