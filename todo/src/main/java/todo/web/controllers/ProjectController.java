package todo.web.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.service.api.ProjectService;
import todo.service.dto.ProjectDto;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/projects")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<ProjectDto> getProjectByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(projectService.getProjectByName(name).orElse(new ProjectDto()));
    }

    @GetMapping
    public ResponseEntity<Page<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping
    public ResponseEntity<ProjectDto> addProject(@RequestBody @Valid ProjectDto projectDto) {
        return ResponseEntity.ok(projectService.insertProject(projectDto).get());
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable String id, @RequestBody @Valid ProjectDto projectDto) {
        return ResponseEntity.ok(projectService.updateProject(id, projectDto).get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteProject(@PathVariable("id") String id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

}
