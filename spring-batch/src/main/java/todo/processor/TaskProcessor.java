package todo.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import todo.dto.TaskDto;

import java.util.Date;

public class TaskProcessor implements ItemProcessor<TaskDto, TaskDto> {
    private static Logger log = LoggerFactory.getLogger(TaskProcessor.class);

    @Override
    public TaskDto process(TaskDto item) throws Exception {
        log.info("Start processing next item...");
        item.setDueDate(new Date().toString());
        item.setDescription(item.getDescription() + "/ Task updated at: " + new Date().toString());
        return item;
    }
}
