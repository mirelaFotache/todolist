package todo.reader.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import todo.dto.TaskDto;

public class FileTaskMapper implements FieldSetMapper<TaskDto> {

    @Override
    public TaskDto mapFieldSet(FieldSet fieldSet) throws BindException {
        TaskDto task = new TaskDto();
        task.setDescription(fieldSet.readString("description"));
        task.setDueDate(fieldSet.readString("dueDate").toString());
        task.setRepeatType(fieldSet.readString("repeatType"));
        return task;
    }
}
