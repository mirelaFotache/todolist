package todo.classifier;

import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;
import todo.dto.TaskDto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskClassifier implements Classifier<TaskDto, ItemWriter<? super TaskDto>> {

    private static final long serialVersionUID = 1L;

    private ItemWriter<TaskDto> evenItemWriter;
    private ItemWriter<TaskDto> oddItemWriter;

    public TaskClassifier(ItemWriter<TaskDto> evenItemWriter, ItemWriter<TaskDto> oddItemWriter) {
        this.evenItemWriter = evenItemWriter;
        this.oddItemWriter = oddItemWriter;
    }

    @Override
    public ItemWriter<? super TaskDto> classify(TaskDto task) {
        Calendar cal = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date;
        try {
            date = format.parse(task.getDateCreated());
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day % 2 == 0 ? evenItemWriter : oddItemWriter;
    }
}
