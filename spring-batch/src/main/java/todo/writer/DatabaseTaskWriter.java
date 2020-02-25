package todo.writer;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import todo.dto.TaskDto;

public class DatabaseTaskWriter extends JdbcBatchItemWriter<TaskDto> {

    public DatabaseTaskWriter(DatabaseWriterSettings dw) {
        databaseItemWriter(dw);
    }

    void databaseItemWriter(DatabaseWriterSettings dw) {
        this.setDataSource(dw.getDataSource());
        this.setJdbcTemplate(dw.getJdbcTemplate());
        this.setSql(dw.getQuery());
        this.setItemPreparedStatementSetter(dw.getItemPreparedStatementSetter());
    }
}
