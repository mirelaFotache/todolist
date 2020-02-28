package todo.web.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.service.api.TaskService;
import todo.service.dto.TaskDto;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(value = "/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<Page<TaskDto>> getTaskByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(taskService.getTaskByName(name));
    }


    @GetMapping(value = "/id/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PutMapping
    public ResponseEntity<TaskDto> addTask(@RequestBody @Valid TaskDto taskDto) {
        return ResponseEntity.ok(taskService.insertTask(taskDto).get());
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable("id") String id, @RequestBody @Valid TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDto).orElse(new TaskDto()));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable("id") String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping(value = "/batch_insert")
    public ResponseEntity<Object> batchInsert() {
        taskService.batchInsert();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/test_caching")
    public ResponseEntity<Object> testCaching() {
        taskService.testCaching();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/test_locking")
    public ResponseEntity<Object> testLocking() {
        taskService.testLocking();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/test_refresh_prop")
    public ResponseEntity<String> testRefreshProperties() {
        return ResponseEntity.ok(taskService.testRefreshProperties());
    }
}
