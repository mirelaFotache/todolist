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
import todo.task.MyTaskOne;
import todo.writer.DatabaseTaskWriter;

import javax.sql.DataSource;

/**
 * OBS: Name of the steps and jobs must be unique at application level !!!
 */
@Configuration
@EnableBatchProcessing
public class DatabaseBatchConfig {

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
    @Qualifier("databaseTaskProcessor")
    ItemProcessor<TaskDto, TaskDto> taskDtoItemProcessor() {
        return new TaskProcessor();
    }

    @Bean
    @Qualifier("databaseTaskWriter")
    ItemWriter<TaskDto> databaseTaskWriter() {
        return new DatabaseTaskWriter(dataSource, jdbcTemplate);
    }

    @Autowired
    @Qualifier("databaseTaskWriter")
    private ItemWriter<TaskDto> databaseTaskWriter;

    @Autowired
    @Qualifier("databaseTaskProcessor")
    private ItemProcessor<TaskDto,TaskDto> databaseTaskProcessor;

    @Bean
    NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public Step dbStepOne() {
        return stepBuilderFactory.get("dbStepOne")
                .<TaskDto, TaskDto>chunk(10) // Retrieve data in chunks of 10 items
                .reader(databaseTaskReader())
                .processor(databaseTaskProcessor)
                .writer(databaseTaskWriter)
                .build();
    }

    @Bean
    public Step dbStepTwo() {
        return stepBuilderFactory.get("dbStepTwo").tasklet(new MyTaskOne()).build();
    }

    @Bean
    public Job databaseJob() {
        return jobBuilderFactory.get("databaseJob")
                .incrementer(new RunIdIncrementer())
                .start(dbStepOne())
                .next(dbStepTwo())
                .build();
    }

}
