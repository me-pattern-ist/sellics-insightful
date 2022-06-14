package com.sellics.sherakhan.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class IngestionCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(IngestionCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    public IngestionCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
           // TODO: Do some stuff here to get models from database.
            Long totalCount = jdbcTemplate.queryForObject("select count(*) from product_ranks", Long.class);
            LOGGER.info("Ingestion {} Completed with Total records {}",
                    jobExecution.getJobInstance().getJobName(), totalCount);
        }
    }
}
