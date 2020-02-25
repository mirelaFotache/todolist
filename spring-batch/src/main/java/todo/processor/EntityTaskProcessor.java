package todo.processor;

import org.springframework.batch.item.ItemProcessor;
import todo.models.Task;

import java.util.Calendar;
import java.util.Date;

public class EntityTaskProcessor implements ItemProcessor<Task, Task> {

    @Override
    public Task process(Task item) {
        final Date currentDate = Calendar.getInstance().getTime();
        item.setDueDate(currentDate);
        item.setCreatedDate(currentDate);
        String oldDescription = item.getDescription();
        final int endIndex = oldDescription.indexOf("Updated");
        String temp = "";
        if (endIndex > 0) {
            temp = oldDescription.substring(0, endIndex);
            item.setDescription(temp + "Updated at: " + currentDate);
        }

        return item;
    }

}