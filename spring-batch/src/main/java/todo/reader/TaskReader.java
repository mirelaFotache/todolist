package todo.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.H2PagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import todo.dto.TaskDto;

import javax.sql.DataSource;
import java.util.List;

public class TaskReader implements ItemReader<TaskDto> {

    private static Logger log = LoggerFactory.getLogger(TaskReader.class);

    private List<TaskDto> taskData;
    int counter = 0;

    public TaskReader(DataSource dataSource) {
        initialize(dataSource);
    }

    @Bean
    public DataSource customDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres?currentSchema=todolist");
        dataSource.setUsername("postgres");
        dataSource.setPassword("admin");

        return dataSource;
    }
    // Create dummy records
    private void initialize(DataSource dataSource) {
        JdbcPagingItemReader<TaskDto> databaseReader = new JdbcPagingItemReader<>();
        databaseReader.setDataSource(dataSource);
        databaseReader.setPageSize(1);
        PagingQueryProvider queryProvider = createQueryProvider();
        databaseReader.setQueryProvider(queryProvider);
        databaseReader.setRowMapper(new BeanPropertyRowMapper<>(TaskDto.class));

        ExecutionContext executionContext = new ExecutionContext();
        databaseReader.open(executionContext);
        try {
            TaskDto primerDto = databaseReader.read();
            while (primerDto != null) {
                taskData.add(databaseReader.read());
            }
            databaseReader.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private PagingQueryProvider createQueryProvider() {
        H2PagingQueryProvider queryProvider = new H2PagingQueryProvider();

        queryProvider.setSelectClause("SELECT id, description, due_date, repeat_type ");
        queryProvider.setFromClause("FROM task");

        return queryProvider;
    }


    @Override
    public TaskDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.info("Start reading next item...");

        if (taskData != null) {
            if (counter < taskData.size()) {
                TaskDto task = taskData.get(counter);
                counter++;
                return task;
            }
        } else {
            return null;
        }
        return null;
    }
}
