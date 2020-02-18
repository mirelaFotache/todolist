package todo.writer;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.Resource;
import todo.dto.TaskDto;

public class FlatFileTaskWriter extends FlatFileItemWriter<TaskDto> {

    private Resource outputResource;

    public FlatFileTaskWriter(Resource outputResource) {
        this.outputResource = outputResource;
        fileTaskWriter();
    }

    private void fileTaskWriter() {

        this.setResource(outputResource);
        //this.setAppendAllowed(true);
        final DelimitedLineAggregator<TaskDto> lineAggregator = new DelimitedLineAggregator<TaskDto>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<TaskDto>() {
                    {
                        //OBS: Names must correspond to the ones from dto class
                        setNames(new String[]{"dateCreated","description", "dueDate", "repeatType"});
                    }
                });
            }
        };
        this.setLineAggregator(lineAggregator);
    }

}
