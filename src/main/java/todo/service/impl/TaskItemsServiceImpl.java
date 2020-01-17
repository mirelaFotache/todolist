package todo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import todo.repository.TaskItemsRepository;
import todo.repository.TaskRepository;
import todo.repository.models.Task;
import todo.repository.models.TaskItems;
import todo.service.api.TaskItemsService;
import todo.service.dto.TaskDto;
import todo.service.dto.TaskItemsAdapter;
import todo.service.dto.TaskItemsDto;
import todo.service.exceptions.InvalidParameterException;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class TaskItemsServiceImpl implements TaskItemsService {
    public static final String TASKITEMSDTO_NOTEMPTY_TASKITEMSID = "taskitemsdto.notempty.taskitemsid";

    @Autowired
    private TaskItemsRepository taskItemsRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Page<TaskItemsDto> getTaskItemsByName(String name) {
        if (!name.isEmpty()) {
            Collection<TaskItems> taskItems = taskItemsRepository.getTasksItemsByName(name);
            List<TaskItemsDto> collect = taskItems.stream().map(TaskItemsAdapter::toDto).collect(Collectors.toList());
            return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by(name)), collect.size());
        }
        return null;
    }

    @Override
    public Page<TaskItemsDto> getAllTasksItems() {
        Iterable<TaskItems> tasks = taskItemsRepository.findAll();
        List<TaskItems> result =
                StreamSupport.stream(tasks.spliterator(), false)
                        .collect(Collectors.toList());
        List<TaskItemsDto> collect = result.stream().map(TaskItemsAdapter::toDto).collect(Collectors.toList());
        return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by("name")), collect.size());
    }

    @Override
    @Transactional
    public Optional<TaskItemsDto> insertTaskItems(TaskItemsDto taskItemsDto) {
        TaskDto taskDto = taskItemsDto.getTask();
        if (taskDto.getId() != null) {
            Optional<Task> taskOptional = taskRepository.findById(UUID.fromString(taskDto.getId()));
            String isNotValidMsg = isValidTaskItems(taskItemsDto, taskOptional);
            if (isNotValidMsg.isEmpty()) {
                if (taskDto.getId() != null) {
                    if (taskOptional.isPresent()) {
                        TaskItems taskItems = TaskItemsAdapter.fromDto(taskItemsDto);
                        taskItems.setTask(taskOptional.get());
                        TaskItems persisted = taskItemsRepository.save(taskItems);
                        return Optional.of(TaskItemsAdapter.toDto(persisted));
                    }
                }
            } else {
                throw new InvalidParameterException(isNotValidMsg);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<TaskItemsDto> updateTaskItems(String id, TaskItemsDto taskItemsDto) {
        TaskDto taskDto = taskItemsDto.getTask();
        if (taskDto.getId() != null) {
            Optional<Task> taskOptional = taskRepository.findById(UUID.fromString(taskDto.getId()));
            String isNotValidMsg = isValidTaskItems(taskItemsDto, taskOptional);
            if (isNotValidMsg.isEmpty() && id != null) {
                Optional<TaskItems> taskItemsOptional = taskItemsRepository.findById(UUID.fromString(id));
                if (taskItemsOptional.isPresent()) {
                    TaskItems taskItems = taskItemsOptional.get();
                    taskItems = TaskItemsAdapter.fromDtoToTaskItems(taskItemsDto, taskItems);
                    return Optional.of(TaskItemsAdapter.toDto(taskItemsRepository.save(taskItems)));
                }
            } else {
                throw new InvalidParameterException(isNotValidMsg);
            }
        }
        return Optional.empty();
    }

    /**
     * Delete task object
     *
     * @param id id
     */
    public void deleteTaskItems(String id) {
        if (id != null) {
            Optional<TaskItems> taskItemsOptional = taskItemsRepository.findById(UUID.fromString(id));
            taskItemsOptional.ifPresent(taskItems -> taskItemsRepository.delete(taskItems));
        }
    }

    public String isValidTaskItems(TaskItemsDto taskItemsDto, Optional<Task> taskOptional) {
        String msg = "";
        msg = validateTaskId(taskOptional, msg);
        return msg;
    }

    private String validateTaskId(Optional<Task> taskOptional, String msg) {
        if (!taskOptional.isPresent())
            msg = TASKITEMSDTO_NOTEMPTY_TASKITEMSID;
        return msg;
    }


}
