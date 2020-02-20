package todo.writer.setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import todo.dto.TaskDto;

import java.sql.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class InsertTaskPreparedStatementSetter implements ItemPreparedStatementSetter<TaskDto> {

    private static Logger log = LoggerFactory.getLogger(InsertTaskPreparedStatementSetter.class);

    int counter = 1;

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

        final int size = item.getTaskItems().size();
        UUID[] ids = new UUID[size];
        Date[] dateCreatedList = new Date[size];
        Boolean[] deletedList = new Boolean[size];
        Date[] dateUpdatedList = new Date[size];
        Boolean[] completedList = new Boolean[size];
        String[] labels = new String[size];

        AtomicInteger index = new AtomicInteger();
        item.getTaskItems().forEach(taskItem -> {
            try {
                ids[index.get()] = UUID.randomUUID();
                dateCreatedList[index.get()] = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(taskItem.getDateCreated()).getTime());
                deletedList[index.get()] = item.getDeleted();
                dateUpdatedList[index.get()] = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(taskItem.getDateUpdate()).getTime());
                completedList[index.get()] = taskItem.getCompleted();
                labels[index.get()] = taskItem.getLabel();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            index.getAndIncrement();
        });

        try {
            //taskItem id
            Array sqlIds = ps.getConnection().createArrayOf("UUID", ids);
            ps.setArray(8, sqlIds);

            // taskItem Date created
            Array sqlDateCreated = ps.getConnection().createArrayOf("DATE", dateCreatedList);
            ps.setArray(9, sqlDateCreated);

            // TaskItem Deleted
            Array sqlDeleted = ps.getConnection().createArrayOf("BOOLEAN", deletedList);
            ps.setArray(10, sqlDeleted);

            // taskItem Date update
            Array sqlDateUpdate = ps.getConnection().createArrayOf("DATE", dateUpdatedList);
            ps.setArray(11, sqlDateUpdate);

            // TaskItem Completed
            Array sqlCompletedList = ps.getConnection().createArrayOf("BOOLEAN", completedList);
            ps.setArray(12, sqlCompletedList);

            // TaskItem Label
            Array sqlLabels = ps.getConnection().createArrayOf("VARCHAR", labels);
            ps.setArray(13, sqlLabels);

            log.info("counter = " + counter);
            counter++;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
