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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import todo.dto.TaskDto;
import todo.processor.TaskProcessor;
import todo.reader.DatabaseTaskReader;
import todo.task.CustomTasklet;
import todo.writer.DatabaseTaskWriter;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @Qualifier("databaseTaskReader")
    ItemReader<TaskDto> databaseTaskReader() {
        return new DatabaseTaskReader(dataSource);
    }

    @Bean
    ItemProcessor<TaskDto, TaskDto> taskDtoItemProcessor() {
        return new TaskProcessor();
    }

    @Bean
    ItemWriter<TaskDto> databaseTaskWriter() {
        return new DatabaseTaskWriter(dataSource, jdbcTemplate);
    }

    @Bean
    NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public Step stepOne() {
        return stepBuilderFactory.get("stepOne")
                .<TaskDto, TaskDto>chunk(10) // Retrieve data in chunks of 10 items
                .reader(databaseTaskReader())
                .processor(taskDtoItemProcessor())
                .writer(databaseTaskWriter())
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
                .start(stepOne())
                .next(stepTwo())
                .build();
    }

}
