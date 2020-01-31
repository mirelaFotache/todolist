package todo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import todo.repository.ProjectRepository;
import todo.repository.models.Project;
import todo.service.api.ProjectService;
import todo.service.dto.ProjectAdapter;
import todo.service.dto.ProjectDto;
import todo.exceptions.InvalidParameterException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ProjectServiceImpl implements ProjectService {

    public static final String LABEL = "projectdto.invalid.parameter.label";

    private ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<ProjectDto> getProjectByName(String name) {
        return Optional.of(ProjectAdapter.toDto(projectRepository.getProjectByName(name)));
    }

    @Override
    public Page<ProjectDto> getAllProjects() {
        List<Project> projects = StreamSupport.stream(projectRepository.findAll().spliterator(), false).collect(Collectors.toList());
        List<ProjectDto> collect = projects.stream().map(ProjectAdapter::toDto).collect(Collectors.toList());
        return new PageImpl<>(collect, PageRequest.of(0, 20, Sort.by("name")), collect.size());
    }

    @Override
    @Transactional
    public Optional<ProjectDto> insertProject(ProjectDto projectDto) {
        String isNotValidMsg = isValidProject(projectDto);
        if (isNotValidMsg.isEmpty()) {
            return Optional.of(ProjectAdapter.toDto(projectRepository.save(ProjectAdapter.fromDto(projectDto))));
        } else {
            throw new InvalidParameterException(isNotValidMsg);
        }
    }

    @Override
    @Transactional
    public Optional<ProjectDto> updateProject(String id, ProjectDto projectDto) {
        String isNotValidMsg = isValidProject(projectDto);
        if (isNotValidMsg.isEmpty() && id != null) {
            Optional<Project> projectOptional = projectRepository.findById(UUID.fromString(id));
            if (projectOptional.isPresent()) {
                projectOptional.get().setLabel(projectDto.getLabel());
                return Optional.of(ProjectAdapter.toDto(projectRepository.save(projectOptional.get())));
            }
        } else {
            throw new InvalidParameterException(isNotValidMsg);
        }
        return Optional.empty();
    }

    @Transactional
    public void deleteProject(String id) {
        if (id != null) {
            Optional<Project> projectOptional = projectRepository.findById(UUID.fromString(id));
            projectOptional.ifPresent(project -> projectRepository.delete(project));
        }
    }

    public String isValidProject(ProjectDto projectDto) {
        String msg = "";
        msg = validateLabel(projectDto, msg);

        return msg;
    }

    private String validateLabel(ProjectDto projectDto, String msg) {
        Project project = projectRepository.getProjectByName(projectDto.getLabel());
        if (project!=null)
            msg = LABEL;
        return msg;
    }

    public ProjectRepository getProjectRepository() {
        return projectRepository;
    }
}
