package todo.service.api;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import todo.service.dto.TaskItemsDto;

import java.util.Optional;

@Service
public interface TaskItemsService {
    public Page<TaskItemsDto> getTaskItemsByName(String name);

    public Page<TaskItemsDto> getAllTasksItems();

    public Optional<TaskItemsDto> insertTaskItems(TaskItemsDto taskItemsDto);

    public Optional<TaskItemsDto> updateTaskItems(String id, TaskItemsDto taskItemsDto);

    public void deleteTaskItems(String id);
}
