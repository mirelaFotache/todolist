package todo.processor;

import org.springframework.batch.item.ItemProcessor;
import todo.dto.TaskDto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskProcessor implements ItemProcessor<TaskDto, TaskDto> {

    @Override
    public TaskDto process(TaskDto item) {

        String today = getDayOfToday();

        item.setDateCreated(today);
        item.setDateUpdate(today);
        item.setDueDate(today);
        final int limit = item.getDescription().indexOf("Updated");
        if (limit > 0) {
            item.setDescription(item.getDescription().substring(0, limit) + " Updated at: " + today);
        } else {
            item.setDescription(item.getDescription() + " Updated at: " + today);
        }

        return item;
    }

    private String getDayOfToday() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
}
