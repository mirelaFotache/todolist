package todo.processor;

import org.springframework.batch.item.ItemProcessor;
import todo.dto.TaskDto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskProcessor implements ItemProcessor<TaskDto, TaskDto> {
    //private static Logger log = LoggerFactory.getLogger(TaskProcessor.class);
    private static int counter = 1;

    @Override
    public TaskDto process(TaskDto item) {

        String today = getDayOfToday();

        item.setDateCreated(today);
        item.setDateUpdate(today);
        item.setDueDate(today);
        item.setDescription("task " + counter + " Updated at: " + today);

        //log.info("Processed item: " + item);
        counter++;

        return item;
    }

    private String getDayOfToday() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
}
