package todo.service.dto;

import org.springframework.context.i18n.LocaleContextHolder;
import todo.repository.models.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskAdapter {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        if (task != null) {
            dto.setId(task.getId().toString());
            dto.setDescription(task.getDescription());
            DateFormat formatter = new SimpleDateFormat(YYYY_MM_DD);
            if (task.getDueDate() != null)
                dto.setDueDate(formatter.format(task.getDueDate()));
            dto.setRepeatType(task.getRepeatType());
            ProjectDto projectDto = new ProjectDto();
            if (task.getProject().getId() != null)
                projectDto.setId(task.getProject().getId().toString());
            projectDto.setLabel(task.getProject().getLabel());
            dto.setProject(projectDto);
            dto.setDeleted(task.getDeleted());
        }
        return dto;
    }

    public static Task fromDto(TaskDto dto) {
        Task task = new Task();
        if (dto != null) {
            if (dto.getId() != null)
                task.setId(UUID.fromString(dto.getId()));
            task.setDescription(dto.getDescription());
            if (dto.getDueDate() != null) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", LocaleContextHolder.getLocale());
                    task.setDueDate(formatter.parse(dto.getDueDate()));
                } catch (ParseException e) {
                    //empty
                }
            }
            task.setRepeatType(dto.getRepeatType());
            task.setProject(ProjectAdapter.fromDto(dto.getProject()));
            if (dto.getDeleted() != null)
                task.setDeleted(dto.getDeleted());
        }
        return task;
    }

    public static Task fromDtoToTask(TaskDto dto, Task task) {
        if (dto != null) {
            task.setDeleted(dto.getDeleted());
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", LocaleContextHolder.getLocale());
                task.setDueDate(formatter.parse(dto.getDueDate()));
            } catch (ParseException e) {
                //Empty
            }
            task.setDescription(dto.getDescription());
            task.setRepeatType(dto.getRepeatType());
            task.setProject(ProjectAdapter.fromDto(dto.getProject()));
            task.setTaskItems(TaskItemsAdapter.fromDtoList(dto.getTaskItems()));
        }
        return task;
    }

    public static List<TaskDto> toDtoList(List<Task> tasks) {
        List<TaskDto> tasksDto = new ArrayList<>();
        for (Task task : tasks) {
            tasksDto.add(toDto(task));
        }
        return tasksDto;
    }

    public static List<Task> fromDtoList(List<TaskDto> tasksDto) {
        List<Task> tasks = new ArrayList<>();
        if (tasksDto != null)
            for (TaskDto dto : tasksDto) {
                tasks.add(fromDto(dto));
            }
        return tasks;
    }
}
