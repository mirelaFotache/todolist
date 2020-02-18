package todo.writer;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import todo.dto.TaskDto;
import todo.writer.setter.TaskPreparedStatementSetter;

import javax.sql.DataSource;

public class DatabaseTaskWriter extends JdbcBatchItemWriter<TaskDto> {

    private DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;
    private static final String QUERY_INSERT_TASK = "update task set description=?, due_date=? where id=? ";

    public DatabaseTaskWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;

        databaseItemWriter(dataSource, jdbcTemplate);
    }

    void databaseItemWriter(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
        this.setDataSource(dataSource);
        this.setJdbcTemplate(jdbcTemplate);
        this.setSql(QUERY_INSERT_TASK);

        ItemPreparedStatementSetter<TaskDto> valueSetter =
                new TaskPreparedStatementSetter();
        this.setItemPreparedStatementSetter(valueSetter);

    }
}
