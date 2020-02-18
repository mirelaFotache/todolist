package todo.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import todo.dto.TaskDto;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTaskReader implements ItemReader<TaskDto> {

    private static Logger log = LoggerFactory.getLogger(DatabaseTaskReader.class);
    private List<TaskDto> taskData = new ArrayList<>();
    int counter = 0;

    public DatabaseTaskReader(DataSource dataSource) {
        final JdbcCursorItemReader<TaskDto> taskDtoItemReader = databaseItemReader(dataSource);
        taskDtoItemReader.open(new ExecutionContext());
        try {
            TaskDto task = taskDtoItemReader.read();
            taskData.add(task);
            log.info("Task: "+task);
            while ( task!= null) {
                task = taskDtoItemReader.read();
                log.info("Task: "+task);
                taskData.add(task);
            }
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    private static final String QUERY_FIND_TASKS =
            "SELECT * FROM TASK where due_date > '202-02-13'";


    JdbcCursorItemReader<TaskDto> databaseItemReader(DataSource dataSource) {
        JdbcCursorItemReader<TaskDto> databaseReader = new JdbcCursorItemReader<>();

        databaseReader.setDataSource(dataSource);
        databaseReader.setSql(QUERY_FIND_TASKS);
        databaseReader.setRowMapper(new BeanPropertyRowMapper<>(TaskDto.class));

        return databaseReader;
    }

    @Override
    public TaskDto read() {
        if (taskData != null) {
            if (counter < taskData.size()) {
                TaskDto task = taskData.get(counter);
                log.info("Start reading next task: "+task);
                counter++;
                return task;
            }
        } else {
            return null;
        }
        return null;
    }
}
