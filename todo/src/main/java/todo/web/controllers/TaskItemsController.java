package todo.web.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.service.api.TaskItemsService;
import todo.service.dto.TaskItemsDto;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/taskitems")
public class TaskItemsController {

    private TaskItemsService taskItemsService;

    public TaskItemsController(TaskItemsService taskItemsService) {
        this.taskItemsService = taskItemsService;
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<Page<TaskItemsDto>> getTaskItemsByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(taskItemsService.getTaskItemsByName(name));
    }

    @GetMapping
    public ResponseEntity<Page<TaskItemsDto>> getAllTaskItems() {
        return ResponseEntity.ok(taskItemsService.getAllTasksItems());
    }

    @PutMapping
    public ResponseEntity<TaskItemsDto> addTaskItems(@RequestBody @Valid TaskItemsDto taskItemsDto) {
        return ResponseEntity.ok(taskItemsService.insertTaskItems(taskItemsDto).orElse(new TaskItemsDto()));
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<TaskItemsDto> updateTaskItems(@PathVariable("id") String id, @RequestBody @Valid TaskItemsDto taskItemsDto) {
        return ResponseEntity.ok(taskItemsService.updateTaskItems(id, taskItemsDto).orElse(new TaskItemsDto()));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteTaskItems(@PathVariable("id") String id) {
        taskItemsService.deleteTaskItems(id);
        return ResponseEntity.noContent().build();
    }
}
