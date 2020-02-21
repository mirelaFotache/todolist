package todo.writer.setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import todo.dto.ProjectDto;
import todo.dto.TaskDto;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class InsertTaskPreparedStatementSetter implements ItemPreparedStatementSetter<TaskDto> {

    private static Logger log = LoggerFactory.getLogger(InsertTaskPreparedStatementSetter.class);

    int counter = 1;

    @Override
    public void setValues(TaskDto task, PreparedStatement ps) throws SQLException {

        ProjectDto project = task.getProject();

        //PROJECT
        // Id
        ps.setObject(1, UUID.randomUUID());

        // Date created
        try {
            ps.setTimestamp(2, new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(project.getDateCreated()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Deleted
        ps.setBoolean(3, project.getDeleted());

        // Date update
        try {
            ps.setTimestamp(4, new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(project.getDateUpdate()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Label
        ps.setString(5, project.getLabel());


        //TASK
        // Id
        ps.setObject(6, UUID.randomUUID());

        // Date created
        try {
            ps.setTimestamp(7, new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(task.getDateCreated()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Deleted
        ps.setBoolean(8, task.getDeleted());

        // Date update
        try {
            ps.setTimestamp(9, new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(task.getDateUpdate()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Description
        ps.setString(10, task.getDescription());

        // Due date
        try {
            ps.setTimestamp(11, new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(task.getDueDate()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Repeat type
        ps.setString(12, task.getRepeatType());


        //TASK ITEM
        final int size = task.getTaskItems().size();
        UUID[] ids = new UUID[size];
        Timestamp[] dateCreatedList = new Timestamp[size];
        Boolean[] deletedList = new Boolean[size];
        Timestamp[] dateUpdatedList = new Timestamp[size];
        Boolean[] completedList = new Boolean[size];
        String[] labels = new String[size];

        AtomicInteger index = new AtomicInteger();
        task.getTaskItems().forEach(taskItem -> {
            try {
                ids[index.get()] = UUID.randomUUID();
                dateCreatedList[index.get()] = new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(taskItem.getDateCreated()).getTime());
                deletedList[index.get()] = taskItem.getDeleted();
                dateUpdatedList[index.get()] = new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(taskItem.getDateUpdate()).getTime());
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
            ps.setArray(13, sqlIds);

            // taskItem Date created
            Array sqlDateCreated = ps.getConnection().createArrayOf("DATE", dateCreatedList);
            ps.setArray(14, sqlDateCreated);

            // TaskItem Deleted
            Array sqlDeleted = ps.getConnection().createArrayOf("BOOLEAN", deletedList);
            ps.setArray(15, sqlDeleted);

            // taskItem Date update
            Array sqlDateUpdate = ps.getConnection().createArrayOf("DATE", dateUpdatedList);
            ps.setArray(16, sqlDateUpdate);

            // TaskItem Completed
            Array sqlCompletedList = ps.getConnection().createArrayOf("BOOLEAN", completedList);
            ps.setArray(17, sqlCompletedList);

            // TaskItem Label
            Array sqlLabels = ps.getConnection().createArrayOf("VARCHAR", labels);
            ps.setArray(18, sqlLabels);

            log.info("counter = " + counter);
            counter++;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
