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
public class CartItemJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public FlatFileItemReader<CartItem> cartItemReader() {
        return new FlatFileItemReaderBuilder<CartItem>()
                .name("cartItemReader")
                .resource(new FileSystemResource("input/cartitems.csv"))
                .linesToSkip(0)
                .lineMapper(new DefaultLineMapper<>() {{
                    setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames("id", "pc", "quantity");
                    }});
                    setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                        setTargetType(CartItem.class);
                    }});
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<CartItem> cartItemWriter() {
        JpaItemWriter<CartItem> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step cartItemStep() {
        return new StepBuilder("cartItemStep", jobRepository)
                .<CartItem, CartItem>chunk(10, transactionManager)
                .reader(cartItemReader())
                .writer(cartItemWriter())
                .build();
    }

    @Bean
    public Job cartItemJob() {
        return new JobBuilder("cartItemJob", jobRepository)
                .start(cartItemStep())
                .build();
    }
}
