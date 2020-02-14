package todo.mapper;

import org.springframework.jdbc.core.RowMapper;
import todo.dto.TaskDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskMapper implements RowMapper<TaskDto> {
    @Override
    public TaskDto mapRow(ResultSet rs, int i) throws SQLException {
        TaskDto task = new TaskDto();
        task.setId(rs.getString("id"));
        task.setDescription(rs.getString("description"));
        task.setDueDate(rs.getDate("due_date").toString());
        task.setRepeatType(rs.getString("repeat_type"));
        return task;
    }
}
