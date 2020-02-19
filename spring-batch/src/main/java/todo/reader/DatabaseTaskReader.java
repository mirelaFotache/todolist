package todo.reader;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import todo.dto.TaskDto;

import javax.sql.DataSource;

public class DatabaseTaskReader extends JdbcCursorItemReader<TaskDto> {

    private DataSource dataSource;
    private String query;

    public DatabaseTaskReader(DataSource dataSource, String query) {
        this.dataSource = dataSource;
        this.query = query;

        databaseItemReader();
    }

    void databaseItemReader() {
        this.setDataSource(dataSource);
        this.setSql(query);
        this.setRowMapper(new BeanPropertyRowMapper<>(TaskDto.class));
    }

}
