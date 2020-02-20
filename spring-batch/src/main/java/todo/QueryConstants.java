package todo;

public interface QueryConstants {

    String QUERY_UPDATE_TASK = "UPDATE task SET description=?, due_date=? WHERE id=? ";
    String QUERY_INSERT_TASK = "WITH insTask AS (\n" +
            "   INSERT INTO task (id, date_created, deleted, date_update, description, due_date, repeat_type) \n" +
            "   VALUES (?, ?, ?, ?, ?, ?, ?) \n" +
            "   ON     CONFLICT DO NOTHING \n" +
            "   RETURNING id AS task_id\n" +
            "   )\n" +
            "INSERT INTO task_items (id, date_created, deleted, date_update, completed, label, task_id)\n" +
            "   VALUES (unnest(?), unnest(?), unnest(?), unnest(?), unnest(?), unnest(?), (SELECT task_id FROM insTask)) \n" +
            "   ON     CONFLICT DO NOTHING \n";
    String QUERY_FIND_TASKS_BY_DATE = "SELECT * FROM task WHERE due_date > '202-02-13'";
}
