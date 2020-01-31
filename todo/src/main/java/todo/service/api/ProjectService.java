package todo.service.api;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import todo.service.dto.ProjectDto;

import java.util.Optional;

@Service
public interface ProjectService {

    public Optional<ProjectDto> getProjectByName(String name);

    public Page<ProjectDto> getAllProjects();

    public Optional<ProjectDto> insertProject(ProjectDto projectDto);

    public Optional<ProjectDto> updateProject(String id, ProjectDto projectDto);

    public void deleteProject(String id);
}
