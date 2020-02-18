package todo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import todo.dto.TaskDto;
import todo.processor.TaskProcessor;
import todo.reader.FlatFileTaskReader;
import todo.writer.FlatFileTaskWriter;

@Configuration
@EnableBatchProcessing
public class FileBachConfig {

    private Resource outputResource = new FileSystemResource("csv/output.csv");

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @Qualifier("fileTaskReader")
    public ItemReader<TaskDto> fileTaskReader() {
        return new FlatFileTaskReader();
    }

    @Bean
    @Qualifier("fileTaskProcessor")
    ItemProcessor<TaskDto, TaskDto> fileTaskProcessor() {
        return new TaskProcessor();
    }

    @Bean
    @Qualifier("fileTaskWrite")
    public FlatFileItemWriter<TaskDto> flatFileWriter() {
        return new FlatFileTaskWriter(outputResource);
    }

    @Autowired
    @Qualifier("fileTaskReader")
    private ItemReader<TaskDto> fileTaskReader;

    @Autowired
    @Qualifier("fileTaskProcessor")
    private ItemProcessor<TaskDto, TaskDto> fileTaskProcessor;

    @Autowired
    @Qualifier("fileTaskWrite")
    private ItemWriter<TaskDto> fileTaskWriter;

    @Bean
    public Step stepThree() {
        return stepBuilderFactory.get("stepThree")
                // Retrieve data in chunks of 10 items
                .<TaskDto, TaskDto>chunk(10)
                .reader(fileTaskReader)
                .processor(fileTaskProcessor)
                .writer(fileTaskWriter)
                .build();
    }

    @Bean
    public Job demo2Job() {
        return jobBuilderFactory.get("demo2Job")
                .incrementer(new RunIdIncrementer())
                .start(stepThree())
                .build();
    }

}
