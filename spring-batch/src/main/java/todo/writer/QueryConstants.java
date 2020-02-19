package todo.writer;

public interface QueryConstants {

    String QUERY_UPDATE_TASK = "UPDATE task SET description=?, due_date=? WHERE id=? ";
    String QUERY_INSERT_TASK = "INSERT INTO task (description, due_date) VALUES (?, ?) ";
}
