package todo.writer.setter;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import todo.dto.TaskDto;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class InsertTaskPreparedStatementSetter implements ItemPreparedStatementSetter<TaskDto> {

    @Override
    public void setValues(TaskDto item, PreparedStatement ps) throws SQLException {

        // Id
        ps.setObject(1, UUID.randomUUID());

        // Date created
        try {
            ps.setDate(2, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(item.getDateCreated()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Deleted
        ps.setBoolean(3, item.getDeleted());

        // Date update
        try {
            ps.setDate(4, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(item.getDateUpdate()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Description
        ps.setString(5, item.getDescription());

        // Due date
        try {
            ps.setDate(6, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(item.getDueDate()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Repeat type
        ps.setString(7, item.getRepeatType());

        // Project id
        if(item.getProject()!=null)
            ps.setString(8, item.getProject().getId());
    }
}
