package todo.service.dto;

import todo.repository.models.Project;

import java.util.*;

public class ProjectAdapter {

    public static ProjectDto toDto(Project project) {
        ProjectDto dto = new ProjectDto();
        if (project != null) {
            dto.setId(project.getId().toString());
            dto.setLabel(project.getLabel());
        }
        return dto;
    }

    public static Project fromDto(ProjectDto dto) {
        Project project = new Project();
        if (dto != null) {
            if (dto.getId() != null && !dto.getId().isEmpty())
                project.setId(UUID.fromString(dto.getId()));
            project.setLabel(dto.getLabel());
            project.setTasks(TaskAdapter.fromDtoList(dto.getTasks()));
            project.setUsers(UserAdapter.fromDtoSet(dto.getUsers()));
        }
        return project;
    }

    public static Set<ProjectDto> toDtoSet(Set<Project> projects) {
        Set<ProjectDto> projectsDto = new HashSet<>();
        if (projects!=null && !projects.isEmpty()) {
            for (Project o : projects) {
                projectsDto.add(toDto(o));
            }
        }
        return projectsDto;
    }

    public static Set<Project> fromDtoSet(Set<ProjectDto> projectsDto) {
        Set<Project> projects = new HashSet<>();
        if (projectsDto != null && !projectsDto.isEmpty()) {
            for (ProjectDto o : projectsDto) {
                projects.add(fromDto(o));
            }
        }
        return projects;
    }

}
