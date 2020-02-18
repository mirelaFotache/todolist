package todo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import todo.task.MyTaskOne;
import todo.task.MyTaskTwo;

@Configuration
@EnableBatchProcessing
public class QuartzBatchConfig {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public Step quartzStepOne(){
        return steps.get("quartzStepOne")
                .tasklet(new MyTaskOne())
                .build();
    }

    @Bean
    public Step quartzStepTwo(){
        return steps.get("quartzStepTwo")
                .tasklet(new MyTaskTwo())
                .build();
    }

    @Bean(name="quartzJobOne")
    public Job quartzJobOne(){
        return jobs.get("quartzJobOne")
                .start(quartzStepOne())
                .next(quartzStepTwo())
                .build();
    }

    @Bean(name="quartzJobTwo")
    public Job quartzJobTwo(){
        return jobs.get("quartzJobTwo")
                .flow(quartzStepOne())
                .build()
                .build();
    }
}
