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
public class OrderItemJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public FlatFileItemReader<OrderItem> orderItemReader() {
        return new FlatFileItemReaderBuilder<OrderItem>()
                .name("orderItemReader")
                .resource(new FileSystemResource("input/orderitems.csv"))
                .linesToSkip(0)
                .lineMapper(new DefaultLineMapper<>() {{
                    setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames("id", "order", "pc", "quantity");
                    }});
                    setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                        setTargetType(OrderItem.class);
                    }});
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<OrderItem> orderItemWriter() {
        JpaItemWriter<OrderItem> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step orderItemStep() {
        return new StepBuilder("orderItemStep", jobRepository)
                .<OrderItem, OrderItem>chunk(10, transactionManager)
                .reader(orderItemReader())
                .writer(orderItemWriter())
                .build();
    }

    @Bean
    public Job orderItemJob() {
        return new JobBuilder("orderItemJob", jobRepository)
                .start(orderItemStep())
                .build();
    }
}
