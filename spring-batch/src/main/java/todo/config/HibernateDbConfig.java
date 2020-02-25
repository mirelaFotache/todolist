package todo.config;

import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import todo.QueryConstants;
import todo.models.Task;
import todo.processor.EntityTaskProcessor;

import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableBatchProcessing
public class HibernateDbConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("todo.models");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(
            SessionFactory sessionFactory) {
        return new HibernateTransactionManager(
                sessionFactory);
    }

    @Autowired
    SessionFactory sessionFactory;

    @Bean
    @Qualifier("databaseEntityTaskReader")
    HibernateCursorItemReader<Task> databaseEntityTaskReader() {
        Map<String, Object> parameters = new HashMap<>();
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-25");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        parameters.put("currentDate", date);

        HibernateCursorItemReader<Task> itemReader = new HibernateCursorItemReader<>();
        itemReader.setQueryString(QueryConstants.QUERY_FIND_TASKS_BY_DATE_HB);
        itemReader.setParameterValues(parameters);
        itemReader.setSessionFactory(sessionFactory);
        itemReader.setUseStatelessSession(true);//with false return error
        return itemReader;
    }

    @Autowired
    @Qualifier("databaseEntityTaskReader")
    private ItemReader<Task> databaseEntityTaskReader;

    @Bean
    @Qualifier("databaseEntityTaskProcessor")
    ItemProcessor<Task, Task> databaseEntityTaskProcessor() {
        return new EntityTaskProcessor();
    }

    @Autowired
    @Qualifier("databaseEntityTaskProcessor")
    private ItemProcessor<Task, Task> databaseEntityTaskProcessor;

    @Bean
    @Qualifier("databaseEntityTaskWriter")
    public ItemWriter<Task> databaseEntityTaskWriter() {
        HibernateItemWriter<Task> itemWriter = new HibernateItemWriter<>();
        itemWriter.setSessionFactory(sessionFactory);
        return itemWriter;
    }

    @Autowired
    @Qualifier("databaseEntityTaskWriter")
    private ItemWriter<Task> databaseEntityTaskWriter;

    @Bean
    public Step dbStepFour() {
        return stepBuilderFactory.get("dbStepFour")
                .<Task, Task>chunk(10) // Retrieve data in chunks of 10 items
                .reader(databaseEntityTaskReader)
                .processor(databaseEntityTaskProcessor)
                .writer(databaseEntityTaskWriter)
                .build();
    }

    @Bean
    public Job databaseJob() {
        return jobBuilderFactory.get("dbHibernateJob")
                .incrementer(new RunIdIncrementer())
                .start(dbStepFour())
                .build();
    }
}
