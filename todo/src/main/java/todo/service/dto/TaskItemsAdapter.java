package todo.service.dto;

import todo.repository.models.TaskItems;

import java.util.ArrayList;
import java.util.List;

public class TaskItemsAdapter {

    public static TaskItemsDto toDto(TaskItems items) {
        TaskItemsDto dto = new TaskItemsDto();
        if (items != null) {
            dto.setId(items.getId());
            dto.setLabel(items.getLabel());
            dto.setCompleted(items.getCompleted());
            dto.setTask(TaskAdapter.toDto(items.getTask()));
        }
        return dto;
    }

    public static TaskItems fromDto(TaskItemsDto dto) {
        TaskItems items = new TaskItems();
        if (dto != null) {
            items.setCompleted(dto.getCompleted());
            items.setLabel(dto.getLabel());
            items.setTask(TaskAdapter.fromDto(dto.getTask()));
        }
        return items;
    }

    public static TaskItems fromDtoToTaskItems(TaskItemsDto dto, TaskItems items) {
        if (dto != null) {
            items.setCompleted(dto.getCompleted());
            items.setLabel(dto.getLabel());
            items.setTask(TaskAdapter.fromDto(dto.getTask()));
        }
        return items;
    }

    public static List<TaskItems> fromDtoList(List<TaskItemsDto> dtos) {
        List<TaskItems> taskItems = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            dtos.forEach(t -> taskItems.add(TaskItemsAdapter.fromDto(t)));
        }
        return taskItems;
    }
    public static List<TaskItemsDto> toDtoList(List<TaskItems> items) {
        List<TaskItemsDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            items.forEach(t -> {
                t.setTask(null);
                dtos.add(TaskItemsAdapter.toDto(t));
            });
        }
        return dtos;
    }
}
