package todo.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import todo.dto.TaskDto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskProcessor implements ItemProcessor<TaskDto, TaskDto> {
    private static Logger log = LoggerFactory.getLogger(TaskProcessor.class);
    private static int counter = 1;

    @Override
    public TaskDto process(TaskDto item) throws Exception {

        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);

        item.setDueDate(todayAsString);
        item.setDescription("task "+counter+" Updated at: " + todayAsString);
        counter++;
        log.info("Processed item: "+item);
        return item;
    }
}
