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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import todo.dto.TaskDto;
import todo.processor.TaskProcessor;
import todo.reader.TaskReader;
import todo.task.CustomTasklet;
import todo.writer.TaskWriter;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    ItemReader<TaskDto> taskDtoItemReader() {
        return new TaskReader(dataSource);
    }

    @Bean
    ItemProcessor<TaskDto, TaskDto> taskDtoItemProcessor() {
        return new TaskProcessor();
    }

    @Bean
    ItemWriter<TaskDto> taskDtoItemWriter() {
        return new TaskWriter();
    }

    @Bean
    public Step stepOne(ItemReader<TaskDto> reader,
                        ItemProcessor<TaskDto, TaskDto> processor,
                        ItemWriter<TaskDto> writer) {
        return stepBuilderFactory.get("stepOne")
                .<TaskDto, TaskDto>chunk(10) // Retrieve data in chunks of 10 items
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step stepTwo() {
        return stepBuilderFactory.get("stepTwo").tasklet(new CustomTasklet()).build();
    }

    @Bean
    public Job demoJob() {
        return jobBuilderFactory.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .start(stepOne(new TaskReader(dataSource), new TaskProcessor(), new TaskWriter()))
                .next(stepTwo())
                .build();
    }

}
