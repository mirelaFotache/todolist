package todo;

public interface QueryConstants {

    String QUERY_UPDATE_TASK = "UPDATE task SET description=?, due_date=? WHERE id=? ";
    String QUERY_INSERT_TASK = "INSERT INTO task (description, due_date) VALUES (?, ?) ";
    String QUERY_FIND_TASKS_BY_DATE = "SELECT * FROM task WHERE due_date > '202-02-13'";
}
