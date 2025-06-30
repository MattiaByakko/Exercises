package com.example.app2batch.config;

import com.example.pcshop.model;
import com.example.pcshop.repository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class OrderJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public FlatFileItemReader<Order> orderReader() {
        return new FlatFileItemReaderBuilder<Order>()
                .name("orderItemReader")
                .resource(new FileSystemResource("input/orders.csv"))
                .linesToSkip(0)
                .lineMapper(new DefaultLineMapper<>() {{
                    setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames("id", "cliente", "items");
                    }});
                    setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                        setTargetType(Order.class);
                    }});
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<Order> orderWriter() {
        JpaItemWriter<Order> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step orderStep() {
        return new StepBuilder("orderStep", jobRepository)
                .<Order, Order>chunk(10, transactionManager)
                .reader(orderReader())
                .writer(orderWriter())
                .build();
    }

    @Bean
    public Job orderJob() {
        return new JobBuilder("orderJob", jobRepository)
                .start(orderStep())
                .build();
    }
}
