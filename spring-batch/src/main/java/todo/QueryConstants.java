package todo;

public interface QueryConstants {

    String QUERY_UPDATE_TASK = "UPDATE task SET description=?, due_date=? WHERE id=? ";

    String QUERY_FIND_TASKS_BY_DATE = "SELECT * FROM task WHERE due_date >= '2020-02-25' limit 2";
    String QUERY_FIND_TASKS_BY_DATE_HB = "FROM todo.models.Task t WHERE t.dueDate >= :currentDate";

    String QUERY_INSERT_TASK = " WITH insProject AS (\n" +
            "   INSERT INTO project (id, date_created, deleted, date_update, label)\n" +
            "     VALUES (?, ?, ?, ?, ?)\n" +
            "     ON CONFLICT DO NOTHING\n" +
            "     RETURNING id AS project_id ),\n" +
            "\n" +
            "  insTask AS (\n" +
            "   INSERT INTO task (id, date_created, deleted, date_update, description, due_date, repeat_type, project_id)\n" +
            "     VALUES (?, ?, ?, ?, ?, ?, ?, (SELECT project_id FROM insProject ))\n" +
            "     ON CONFLICT DO NOTHING\n" +
            "     RETURNING id AS task_id )\n" +
            "\n" +
            "   INSERT INTO task_items (id, date_created, deleted, date_update, completed, label, task_id)\n" +
            "     VALUES (unnest(?), unnest(?), unnest(?), unnest(?), unnest(?), unnest(?), (SELECT task_id FROM insTask))\n" +
            "     ON CONFLICT DO NOTHING";
}
