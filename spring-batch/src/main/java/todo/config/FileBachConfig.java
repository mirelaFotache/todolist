package todo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import todo.classifier.TaskClassifier;
import todo.dto.TaskDto;
import todo.processor.TaskProcessor;
import todo.reader.FlatFileTaskReader;
import todo.writer.FlatFileTaskWriter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class FileBachConfig {

    private Resource outputResource = new FileSystemResource("csv/output.csv");
    private Resource outputStdResource = new FileSystemResource("csv/output-std");
    private Resource outputXmlResource = new FileSystemResource("csv/output-xml");

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
    @Qualifier("fileTaskWriter")
    public FlatFileItemWriter<TaskDto> fileTaskWriter() {
        return new FlatFileTaskWriter(outputResource);
    }

    @Autowired
    @Qualifier("fileTaskReader")
    private ItemReader<TaskDto> fileTaskReader;

    @Autowired
    private ItemProcessor<TaskDto, TaskDto> fileTaskProcessor;

    @Autowired
    @Qualifier("fileTaskWriter")
    private ItemWriter<TaskDto> fileTaskWriter;

    @Bean
    @Qualifier("stdFileTaskWrite")
    public FlatFileItemWriter<TaskDto> stdItemWriter() throws Exception {

        FlatFileItemWriter<TaskDto> writer = new FlatFileItemWriter<>();
        writer.setResource(outputStdResource);
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
        writer.open(new ExecutionContext());
        writer.setLineAggregator(lineAggregator);
        writer.afterPropertiesSet();
        return writer;
    }
    @Bean
    public StaxEventItemWriter<TaskDto> xmlItemWriter() throws Exception {

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("task", TaskDto.class);
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliases);

        // StAX and Marshaller for serializing object to XML.
        StaxEventItemWriter<TaskDto> writer = new StaxEventItemWriter<>();
        writer.setResource(outputXmlResource);
        writer.setRootTagName("tasks");
        writer.setMarshaller(marshaller);
        writer.afterPropertiesSet();
        return writer;
    }

    @Bean
    @Qualifier("classifierWriter")
    public ClassifierCompositeItemWriter<TaskDto> classifierCustomerCompositeItemWriter() throws Exception {
        ClassifierCompositeItemWriter<TaskDto> compositeItemWriter = new ClassifierCompositeItemWriter<>();
        compositeItemWriter.setClassifier(new TaskClassifier(stdItemWriter(), xmlItemWriter()));
        return compositeItemWriter;
    }

    // Write the output to output.csv
    @Bean
    public Step fileStepOne() {
        return stepBuilderFactory.get("fileStepOne")
                // Retrieve data in chunks of 10 items
                .<TaskDto, TaskDto>chunk(10)
                .reader(fileTaskReader)
                .processor(fileTaskProcessor)
                .writer(fileTaskWriter)
                .build();
    }

    // Split the output into two files: output-std and output-xml
    @Bean
    public Step fileStepTwo() throws Exception {
        return stepBuilderFactory.get("fileStepTwo")
                // Retrieve data in chunks of 10 items
                .<TaskDto, TaskDto>chunk(10)
                .reader(fileTaskReader)
                .processor(fileTaskProcessor)
                .writer(classifierCustomerCompositeItemWriter())
                .stream(stdItemWriter())
                .stream(xmlItemWriter())
                .build();
    }

    @Bean
    public Job fileJob() throws Exception {
        return jobBuilderFactory.get("fileJob")
                .incrementer(new RunIdIncrementer())
                .start(fileStepOne())
                .next(fileStepTwo())
                .build();
    }

}
