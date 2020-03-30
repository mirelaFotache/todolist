package todo.web.dto;

import todo.data.model.Task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        if (task != null) {
            dto.setId(task.getId());
            dto.setDescription(task.getDescription());
            DateFormat formatter = new SimpleDateFormat(YYYY_MM_DD);
            if (task.getDueDate() != null)
                dto.setDueDate(formatter.format(task.getDueDate()));
            dto.setRepeatType(task.getRepeatType());
            dto.setDone(task.getDone());
            dto.setProjectName(task.getProjectName());
        }
        return dto;
    }

    public static Task fromDto(TaskDto dto) {
        Task task = new Task();
        if (dto != null) {
            if (dto.getId() != null)
                task.setId(dto.getId());
            task.setDescription(dto.getDescription());
            if (dto.getDueDate() != null) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD);
                    task.setDueDate(formatter.parse(dto.getDueDate()));
                } catch (ParseException e) {
                    //empty
                }
            }
            task.setRepeatType(dto.getRepeatType());
            task.setDone(dto.getDone());
            task.setProjectName(dto.getProjectName());
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
