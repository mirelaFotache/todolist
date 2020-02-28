package todo.service.api;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import todo.service.dto.TaskDto;

import java.util.Optional;
import java.util.UUID;

@Service
public interface TaskService {

    public Page<TaskDto> getTaskByName(String name);

    public TaskDto getTaskById(UUID id);

    public Page<TaskDto> getAllTasks();

    public Optional<TaskDto> insertTask(TaskDto taskDto);

    public Optional<TaskDto> updateTask(String id, TaskDto taskDto);

    public void deleteTask(String id);

    public void batchInsert();

    public void testCaching();

    public void testLocking();

    public String testRefreshProperties();
}
