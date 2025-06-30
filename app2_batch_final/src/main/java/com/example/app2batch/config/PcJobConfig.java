package com.example.app2batch.config;

import com.example.pcshop.model;
import com.example.pcshop.repository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
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

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.repository.JobRepository;

@Configuration
@EnableBatchProcessing
public class PcJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public FlatFileItemReader<Pc> pcReader() {
        return new FlatFileItemReaderBuilder<Pc>()
                .name("pcItemReader")
                .resource(new FileSystemResource("input/pcs.csv"))
                .linesToSkip(0)
                .lineMapper(new DefaultLineMapper<Pc>() {{
                    setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames("id", "marca", "modello", "cpu", "ram", "storage", "condizione", "prezzo", "webcam");
                    }});
                    setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                        setTargetType(Pc.class);
                    }});
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<Pc> pcWriter() {
        JpaItemWriter<Pc> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step pcStep() {
        return new StepBuilder("pcStep", jobRepository)
                .<Pc, Pc>chunk(10, transactionManager)
                .reader(pcReader())
                .writer(pcWriter())
                .build();
    }

    @Bean
    public Job pcJob() {
        return new JobBuilder("pcJob", jobRepository)
                .start(pcStep())
                .build();
    }
}
