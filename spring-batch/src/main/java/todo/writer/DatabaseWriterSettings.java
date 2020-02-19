package todo.writer;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import todo.dto.TaskDto;

import javax.sql.DataSource;

public class DatabaseWriterSettings {

    private DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;
    private String query;
    private ItemPreparedStatementSetter<TaskDto> itemPreparedStatementSetter;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ItemPreparedStatementSetter<TaskDto> getItemPreparedStatementSetter() {
        return itemPreparedStatementSetter;
    }

    public void setItemPreparedStatementSetter(ItemPreparedStatementSetter<TaskDto> itemPreparedStatementSetter) {
        this.itemPreparedStatementSetter = itemPreparedStatementSetter;
    }
}
