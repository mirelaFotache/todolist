package todo.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import todo.dto.TaskDto;
import todo.reader.mapper.FileTaskMapper;

public class FlatFileTaskReader extends FlatFileItemReader<TaskDto> {

    public FlatFileTaskReader(){
        fileTaskReader();
    }

    private void fileTaskReader(){
        this.setResource(new FileSystemResource("csv/input.csv"));
        this.setLinesToSkip(1);
        final DefaultLineMapper<TaskDto> lineMapper = new DefaultLineMapper<TaskDto>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("description", "dueDate", "repeatType");
                    }
                });
                setFieldSetMapper(new FileTaskMapper());
            }
        };
        this.setLineMapper(lineMapper);
    }
}
