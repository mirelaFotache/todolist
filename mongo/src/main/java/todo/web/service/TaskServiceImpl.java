package todo.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import todo.data.model.Task;
import todo.data.repository.TaskRepository;
import todo.web.dto.TaskAdapter;
import todo.web.dto.TaskDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private MongoTemplate mongoTemplate;

    private TaskRepository taskRepository;

    public TaskServiceImpl() {
        //Empty
    }

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDto> getTasksByProjectName(String projectName) {
        if (projectName != null && !projectName.isEmpty())
            return TaskAdapter.toDtoList(taskRepository.findTaskByProjectName(projectName));
        else {
            Pageable sortedByDescriptionDescProjectNameAsc =
                    PageRequest.of(0, 2, Sort.by("description").descending().and(Sort.by("projectName")));
            return TaskAdapter.toDtoList(taskRepository.findAll(sortedByDescriptionDescProjectNameAsc).toList());
        }
    }

    public List<TaskDto> getTasksByDate(String dateStr) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            Query query = new Query();
            query.addCriteria(Criteria.where("dueDate").gte(date)).fields().include("projectName").include("description").exclude("id");
            return TaskAdapter.toDtoList(mongoTemplate.find(query, Task.class));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<TaskDto> getTasksByDateBetween(String dateStrGte, String dateStrLte) {
        try {
            Date dateGte = new SimpleDateFormat("yyyy-MM-dd").parse(dateStrGte);
            Date dateLte = new SimpleDateFormat("yyyy-MM-dd").parse(dateStrLte);
            return TaskAdapter.toDtoList(taskRepository.findTaskByDueDate(dateGte, dateLte));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public TaskDto upsertTask(TaskDto taskDto) {
        final Task task = taskRepository.save(TaskAdapter.fromDto(taskDto));
        return TaskAdapter.toDto(task);
    }

    public Boolean deleteTask(String id) {
        taskRepository.deleteById(id);
        return true;
    }
}
