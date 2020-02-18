package todo.writer.setter;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import todo.dto.TaskDto;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TaskPreparedStatementSetter implements ItemPreparedStatementSetter<TaskDto> {
    @Override
    public void setValues(TaskDto item, PreparedStatement ps) throws SQLException {

        // Description
        ps.setString(1, item.getDescription());
        // Due date
        try {
            ps.setTimestamp(2, new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(item.getDueDate()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Id
        ps.setString(3, item.getId());
    }
}
