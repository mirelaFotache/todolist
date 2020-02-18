package todo.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import todo.dto.TaskDto;
import todo.writer.setter.TaskPreparedStatementSetter;

import javax.sql.DataSource;
import java.util.List;

public class DatabaseTaskWriter implements ItemWriter<TaskDto> {

    private DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;
    private static final String QUERY_INSERT_TASK = "update task set description=?, due_date=? where id=? ";

    private static Logger log = LoggerFactory.getLogger(DatabaseTaskWriter.class);

    public DatabaseTaskWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(List<? extends TaskDto> items) throws Exception {
        log.info("Start persisting tasks to database...");
        databaseItemWriter(dataSource, jdbcTemplate).write(items);
    }

    JdbcBatchItemWriter<TaskDto> databaseItemWriter(DataSource dataSource,
                                                    NamedParameterJdbcTemplate jdbcTemplate) {
        JdbcBatchItemWriter<TaskDto> databaseItemWriter = new JdbcBatchItemWriter<>();
        databaseItemWriter.setDataSource(dataSource);
        databaseItemWriter.setJdbcTemplate(jdbcTemplate);
        databaseItemWriter.setSql(QUERY_INSERT_TASK);

        ItemPreparedStatementSetter<TaskDto> valueSetter =
                new TaskPreparedStatementSetter();
        databaseItemWriter.setItemPreparedStatementSetter(valueSetter);

        return databaseItemWriter;
    }
}
