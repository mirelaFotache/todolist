package todo.service.impl;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import todo.exceptions.InvalidParameterException;
import todo.repository.ProjectRepository;
import todo.repository.TaskItemsRepository;
import todo.repository.TaskRepository;
import todo.repository.models.Project;
import todo.repository.models.Task;
import todo.repository.models.TaskItems;
import todo.service.api.TaskService;
import todo.service.dto.*;
import todo.service.dto.enums.RepeatType;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    public static final String TASKDTO_NOTEMPTY_PROJECTID = "taskdto.notempty.projectid";
    public static final String TASKDTO_INVALID_REPEATTYPE = "taskdto.invalid.repeattype";
    public static final String TASKDTO_NOTVALID_DUEDATE = "taskdto.notvalid.duedate";
    public static final String TASKDTO_INVALID_PROJECTID = "taskdto.invalid.projectid";
    private static Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskItemsRepository taskItemsRepository;

    @Autowired
    EntityManager entityManager;

    @Value("jpa.properties.hibernate.jdbc.batch_size")
    private String batchSize;

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
        String isNotValidMsg = "";
        Optional<Project> projectOptional = projectRepository.findById(UUID.fromString(projectDto.getId()));
        if (projectOptional.isPresent())
            isNotValidMsg = isValidTask(taskDto, projectOptional.get());
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
        String isNotValidMsg = "";
        if (projectOptional.isPresent())
            isNotValidMsg = isValidTask(taskDto, projectOptional.get());
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

    public String isValidTask(TaskDto taskDto, Project project) {
        String msg = "";
        msg = validateDueDate(taskDto, msg);
        msg = validateProjectId(project, msg);
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

    private String validateProjectId(Project project, String msg) {
        if (project == null)
            return TASKDTO_INVALID_PROJECTID;
        else if (project.getId() == null)
            return TASKDTO_NOTEMPTY_PROJECTID;
        return msg;
    }

    private String validateRepeatType(TaskDto taskDto, String msg) {
        if (!taskDto.getRepeatType().equals(RepeatType.DAILY.name()))
            msg = TASKDTO_INVALID_REPEATTYPE;
        return msg;
    }

    public void batchInsert() {
        AtomicInteger counter = new AtomicInteger(1);
        AtomicInteger itemCounter = new AtomicInteger(1);

        Session session = (Session) entityManager.getDelegate();
        session.setHibernateFlushMode(FlushMode.COMMIT);
        log.info(batchSize);

        Stream.iterate(0, n -> n + 1)
                .limit(10)
                .forEach(x -> {
                    if (session.getTransaction().getStatus() != TransactionStatus.ACTIVE) {
                        session.beginTransaction();
                    }
                    String strDate = getCurrentDate();

                    //Create project
                    ProjectDto project = new ProjectDto();
                    project.setDeleted(false);
                    project.setLabel("ProjectBB" + counter);
                    //Persist project
                    final Project persistedProject = projectRepository.save(ProjectAdapter.fromDto(project));

                    // Create task
                    TaskDto task = new TaskDto();
                    task.setDeleted(false);
                    task.setDescription("TaskBB" + counter);
                    task.setDueDate(strDate);
                    if (counter.intValue() % 2 == 0) {
                        task.setRepeatType("DAILY");
                    } else {
                        task.setRepeatType("WEEKLY");
                    }
                    final Task taskEntity = TaskAdapter.fromDto(task);
                    taskEntity.setProject(persistedProject);
                    //Persist task
                    final Task persistedTask = taskRepository.save(taskEntity);

                    // Create first task item
                    TaskItemsDto firstItem = new TaskItemsDto();
                    firstItem.setDeleted(false);
                    firstItem.setCompleted(true);
                    firstItem.setLabel("ItemBB" + itemCounter.getAndIncrement());
                    // Create second task item`
                    TaskItemsDto SecondItem = new TaskItemsDto();
                    SecondItem.setDeleted(false);
                    SecondItem.setCompleted(true);
                    SecondItem.setLabel("ItemBB" + itemCounter.getAndIncrement());

                    final TaskItems firstItemEntity = TaskItemsAdapter.fromDto(firstItem);
                    firstItemEntity.setTask(persistedTask);
                    final TaskItems secondItemEntity = TaskItemsAdapter.fromDto(SecondItem);
                    secondItemEntity.setTask(persistedTask);
                    //Persist items
                    taskItemsRepository.saveAll(Arrays.asList(firstItemEntity, secondItemEntity));

                    if (counter.get() % 20 == 0) { //10000, same as the JDBC batch size
                        //flush a batch of inserts and release memory:
                        log.info("Counter: " + counter.get());

                        session.flush();
                        session.clear();
                        try {
                            session.getTransaction().commit();
                        }catch(Exception e){
                            //empty }
                        }
                    }
                    counter.getAndIncrement();
                });
    }

    private String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
