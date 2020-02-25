package todo.classifier;

import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;
import todo.dto.TaskDto;

public class TaskClassifier implements Classifier<TaskDto, ItemWriter<? super TaskDto>> {

    private static final long serialVersionUID = 1L;

    private ItemWriter<TaskDto> evenItemWriter;
    private ItemWriter<TaskDto> oddItemWriter;
    private int counter = 0;

    public TaskClassifier(ItemWriter<TaskDto> evenItemWriter, ItemWriter<TaskDto> oddItemWriter) {
        this.evenItemWriter = evenItemWriter;
        this.oddItemWriter = oddItemWriter;
    }

    @Override
    public ItemWriter<? super TaskDto> classify(TaskDto task) {
        counter++;
        return counter % 2 == 0 ? evenItemWriter : oddItemWriter;
    }
}
