package todo.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import todo.dto.TaskDto;
import todo.task.CustomTasklet;

import java.util.List;

public class TaskWriter implements ItemWriter<TaskDto> {
    private static Logger log = LoggerFactory.getLogger(CustomTasklet.class);

    @Override
    public void write(List<? extends TaskDto> items) throws Exception {
        log.info("Start persisting tasks to database...");
    }
}
