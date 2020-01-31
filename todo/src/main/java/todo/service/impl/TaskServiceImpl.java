package todo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import todo.repository.ProjectRepository;
import todo.repository.TaskRepository;
import todo.repository.models.Project;
import todo.repository.models.Task;
import todo.service.api.TaskService;
import todo.service.dto.ProjectDto;
import todo.service.dto.TaskAdapter;
import todo.service.dto.TaskDto;
import todo.service.dto.enums.RepeatType;
import todo.exceptions.InvalidParameterException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class TaskServiceImpl implements TaskService {

    public static final String TASKDTO_NOTEMPTY_PROJECTID = "taskdto.notempty.projectid";
    public static final String TASKDTO_INVALID_REPEATTYPE = "taskdto.invalid.repeattype";
    public static final String TASKDTO_NOTVALID_DUEDATE = "taskdto.notvalid.duedate";
    public static final String TASKDTO_INVALID_PROJECTID = "taskdto.invalid.projectid";
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Page<TaskDto> getTaskByName(String name) {
        if (!name.isEmpty()) {
            Collection<Task> tasks = taskRepository.getTasksByName(name);
            List<TaskDto> collect = tasks.stream().map(TaskAdapter::toDto).collect(Collectors.toList());
            return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by(name)), collect.size());
        }
        return null;
    }

    /**
     * @return all taskDto objects from database
     */
    public Page<TaskDto> getAllTasks() {
        Iterable<Task> tasks = taskRepository.findAll();
        List<Task> result =
                StreamSupport.stream(tasks.spliterator(), false)
                        .collect(Collectors.toList());
        List<TaskDto> collect = result.stream().map(TaskAdapter::toDto).collect(Collectors.toList());
        return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by("name")), collect.size());
    }

    /**
     * Persist task to database
     *
     * @param taskDto task dto
     * @return persisted taskDto instance
     */
    @Transactional
    public Optional<TaskDto> insertTask(TaskDto taskDto) {
        ProjectDto projectDto = taskDto.getProject();
        Optional<Project> projectOptional = projectRepository.findById(UUID.fromString(projectDto.getId()));
        String isNotValidMsg = isValidTask(taskDto, projectOptional);
        if (isNotValidMsg.isEmpty()) {
            if (projectDto.getId() != null) {
                if (projectOptional.isPresent()) {
                    Task task = TaskAdapter.fromDto(taskDto);
                    task.setProject(projectOptional.get());
                    Task persisted = taskRepository.save(task);
                    return Optional.of(TaskAdapter.toDto(persisted));
                }
            }
        } else {
            throw new InvalidParameterException(isNotValidMsg);
        }
        throw new InvalidParameterException(isNotValidMsg);
    }

    /**
     * Update task to database
     *
     * @param id      id
     * @param taskDto task dto
     * @return updated taskDto object
     */
    @Transactional
    public Optional<TaskDto> updateTask(String id, TaskDto taskDto) {
        ProjectDto projectDto = taskDto.getProject();
        Optional<Project> projectOptional = projectRepository.findById(UUID.fromString(projectDto.getId()));
        String isNotValidMsg = isValidTask(taskDto, projectOptional);
        if (isNotValidMsg.isEmpty() && id != null) {
            Optional<Task> taskOptional = taskRepository.findById(UUID.fromString(id));
            if (taskOptional.isPresent()) {
                Task task = taskOptional.get();
                task = TaskAdapter.fromDtoToTask(taskDto, task);
                return Optional.of(TaskAdapter.toDto(taskRepository.save(task)));
            }
        } else {
            throw new InvalidParameterException(isNotValidMsg);
        }
        return Optional.empty();
    }

    /**
     * Delete task object
     *
     * @param id id
     */
    public void deleteTask(String id) {
        if (id != null) {
            Optional<Task> taskOptional = taskRepository.findById(UUID.fromString(id));
            taskOptional.ifPresent(task -> taskRepository.delete(task));
        }
    }

    public String isValidTask(TaskDto taskDto, Optional<Project> projectOptional) {
        String msg = "";
        msg = validateDueDate(taskDto, msg);
        msg = validateProjectId(projectOptional, msg);
        msg = validateRepeatType(taskDto, msg);
        return msg;
    }

    private String validateDueDate(TaskDto taskDto, String msg) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", LocaleContextHolder.getLocale());
            formatter.parse(taskDto.getDueDate());
        } catch (ParseException e) {
            msg = TASKDTO_NOTVALID_DUEDATE;
        }
        return msg;
    }

    private String validateProjectId(Optional<Project> projectOptional, String msg) {
        if (!projectOptional.isPresent())
            return TASKDTO_INVALID_PROJECTID;
        else if (projectOptional.get().getId() == null)
            return TASKDTO_NOTEMPTY_PROJECTID;
        return msg;
    }

    private String validateRepeatType(TaskDto taskDto, String msg) {
        if (!taskDto.getRepeatType().equals(RepeatType.DAILY.name()))
            msg = TASKDTO_INVALID_REPEATTYPE;
        return msg;
    }

}
