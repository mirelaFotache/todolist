package todo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.web.dto.TaskDto;
import todo.web.service.TaskService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping(path = "/tasks")
    public ResponseEntity<List<TaskDto>> getTasksByProjectName(@RequestParam(required = false) String projectName) {
        return new ResponseEntity<>(taskService.getTasksByProjectName(projectName), HttpStatus.OK);
    }

    @GetMapping(path = "/tasksbydate/{date}")
    public ResponseEntity<List<TaskDto>> getTasksByDate(@PathVariable String date) {
        return new ResponseEntity<>(taskService.getTasksByDate(date), HttpStatus.OK);
    }

    @GetMapping(path = "/tasksbydatebetween/{dateGte}/{dateLte}")
    public ResponseEntity<List<TaskDto>> getTasksByDateBetween(@PathVariable String dateGte, @PathVariable String dateLte) {
        return new ResponseEntity<>(taskService.getTasksByDateBetween(dateGte, dateLte), HttpStatus.OK);
    }

    @PostMapping
    public HttpStatus insertTask(@RequestBody @Valid TaskDto taskDto) {
        TaskDto task = taskService.upsertTask(taskDto);
        if (task != null && task.getId() != null)
            return HttpStatus.CREATED;
        else
            return HttpStatus.EXPECTATION_FAILED;
    }

    @PutMapping
    public HttpStatus updateTask(@RequestBody @Valid TaskDto taskDto) {
        TaskDto task = taskService.upsertTask(taskDto);
        if (task != null && task.getId() != null)
            return HttpStatus.OK;
        else
            return HttpStatus.EXPECTATION_FAILED;
    }

    @DeleteMapping(path = "/{id}")
    public HttpStatus deleteTask(@PathVariable("id") String id) {
        if (taskService.deleteTask(id))
            return HttpStatus.OK;
        else
            return HttpStatus.EXPECTATION_FAILED;
    }

}
