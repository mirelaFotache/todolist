package todo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import todo.task.ModifyBatchUsersTask;
import todo.task.PersistBatchUsersTask;
import todo.task.ReadBatchUsersTask;

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
    public Step stepOne() {
        return stepBuilderFactory.get("stepOne").tasklet(new ReadBatchUsersTask()).build();
    }

    @Bean
    public Step stepTwo(){
        return stepBuilderFactory.get("stepTwo").tasklet(new ModifyBatchUsersTask()).build();
    }

    @Bean
    public Step stepThree(){
        return stepBuilderFactory.get("stepThree").tasklet(new PersistBatchUsersTask()).build();
    }

    @Bean
    public Job demoJob(){
        return jobBuilderFactory.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .start(stepOne())
                .next(stepTwo())
                .next(stepThree())
                .build();
    }

}
