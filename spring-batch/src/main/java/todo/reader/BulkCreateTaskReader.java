package todo.reader;

import org.springframework.batch.item.ItemReader;
import todo.dto.TaskDto;
import todo.dto.TaskItemsDto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class BulkCreateTaskReader implements ItemReader<TaskDto> {

    //private static Logger log = LoggerFactory.getLogger(BulkCreateTaskReader.class);
    private List<TaskDto> taskData = new ArrayList<>();
    int counter = 0;

    public BulkCreateTaskReader() {
        initialize();
    }

    // Create dummy records
    private void initialize() {
        AtomicInteger counter = new AtomicInteger(1);
        Stream.iterate(0, n -> n + 1)
                .limit(1)
                .forEach(x -> {
                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strDate = dateFormat.format(date);

                    // Create task
                    TaskDto task = new TaskDto();
                    task.setId(UUID.randomUUID().toString());
                    task.setDateCreated(strDate);
                    task.setDeleted(false);
                    task.setDateUpdate(strDate);
                    task.setDescription("Task "+counter);
                    task.setDueDate(strDate);
                    if (counter.intValue() % 2 == 0) {
                        task.setRepeatType("DAILY");
                    } else {
                        task.setRepeatType("WEEKLY");
                    }

                    // Create first task item
                    TaskItemsDto item = new TaskItemsDto();
                    item.setId(UUID.randomUUID());
                    item.setDateCreated(strDate);
                    item.setDeleted(false);
                    item.setDateUpdate(strDate);
                    item.setCompleted(true);
                    item.setLabel("Item "+counter);
                    //item.setTaskId(task.getId());

                    if(task.getTaskItems()==null) {
                        task.setTaskItems(new ArrayList<>());
                    }
                    task.getTaskItems().add(item);

                    // Create second task item
                    TaskItemsDto item2 = new TaskItemsDto();
                    item2.setId(UUID.randomUUID());
                    item2.setDateCreated(strDate);
                    item2.setDeleted(false);
                    item2.setDateUpdate(strDate);
                    item2.setCompleted(true);
                    item2.setLabel("Item 2");
                    //item2.setTaskId(task.getId());

                    if(task.getTaskItems()==null) {
                        task.setTaskItems(new ArrayList<>());
                    }
                    task.getTaskItems().add(item2);

                    taskData.add(task);
                    counter.getAndIncrement();
                });
    }

    @Override
    public TaskDto read() {
        //log.info("Start reading next item...");

        if (counter < taskData.size()) {
            TaskDto task = taskData.get(counter);
            counter++;
            return task;
        } else {
            return null;
        }
    }
}
