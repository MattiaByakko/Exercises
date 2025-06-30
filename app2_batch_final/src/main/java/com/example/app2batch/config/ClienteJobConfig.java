package com.example.app2batch.config;

import com.example.pcshop.model;
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
public class ClienteJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public FlatFileItemReader<Cliente> clienteReader() {
        return new FlatFileItemReaderBuilder<Cliente>()
                .name("clienteItemReader")
                .resource(new FileSystemResource("input/clienti.csv"))
                .linesToSkip(0)
                .lineMapper(new DefaultLineMapper<>() {{
                    setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames("id", "email", "nome", "cognome");
                    }});
                    setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                        setTargetType(Cliente.class);
                    }});
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<Cliente> clienteWriter() {
        JpaItemWriter<Cliente> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step clienteStep() {
        return new StepBuilder("clienteStep", jobRepository)
                .<Cliente, Cliente>chunk(10, transactionManager)
                .reader(clienteReader())
                .writer(clienteWriter())
                .build();
    }

    @Bean
    public Job clienteJob() {
        return new JobBuilder("clienteJob", jobRepository)
                .start(clienteStep())
                .build();
    }
}
