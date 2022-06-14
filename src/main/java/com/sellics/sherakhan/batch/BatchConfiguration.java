package com.sellics.sherakhan.batch;

import com.amazonaws.services.s3.AmazonS3;
import com.sellics.sherakhan.batch.model.CSVModel;
import com.sellics.sherakhan.batch.model.IngestionModel;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Environment environment;


    @Bean
    @StepScope
    public FlatFileItemReader<CSVModel> csvReader(AmazonS3 amazonS3Client) throws IOException {
        FlatFileItemReader<CSVModel> reader = new FlatFileItemReader<>();
        S3Resource s3Resource = new S3Resource(
                environment.getProperty("source.s3.bucket.name"),
                environment.getProperty("source.s3.key.name"),
                amazonS3Client);
        reader.setResource(s3Resource);
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<CSVModel>() {{
            setLineTokenizer(new DelimitedLineTokenizer(";") {{
                setNames("timestamp", "asin", "keyword", "rank");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<CSVModel>() {{
                setTargetType(CSVModel.class);
            }});
        }});
        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<IngestionModel> sqlWriter(DataSource dataSource) {
        String sql = "INSERT INTO product_ranks (event_date, serial_number, keywords, rank) " +
                "VALUES (:event_date,  :serial_number,  :keywords,  :rank)";
        return new JdbcBatchItemWriterBuilder<IngestionModel>()
                .itemSqlParameterSourceProvider(new IngestionModelSqlParameterSourceProvider())
                .sql(sql)
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public IngestionModelProcessor modelProcessor() {
        return new IngestionModelProcessor();
    }

    @Bean
    public Step pipelineStep(JdbcBatchItemWriter<IngestionModel> sqlWriter, AmazonS3 amazonS3Client) throws IOException {
        return stepBuilderFactory.get("pipelineStep")
                .<CSVModel, IngestionModel>chunk(5000)
                .reader(csvReader(amazonS3Client))
                .processor(modelProcessor())
                .writer(sqlWriter)
                .build();
    }

    @Bean
    public Job ingestionJob(IngestionCompletionNotificationListener listener, Step pipelineStep) {
        return jobBuilderFactory.get("ingestionJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(pipelineStep)
                .end()
                .build();
    }
}
