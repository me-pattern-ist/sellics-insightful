package com.sellics.sherakhan.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/api/admin")
public class ManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementController.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private Executor executor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
     * This method is used to initiate a batch job asynchronously. Its monitoring url will shared in header parameter
     * as location.
     *
     * curl -i -X POST http://localhost:7171/api/admin/jobs
     *
     */
    @PostMapping("/jobs")
    public ResponseEntity submitIngestJob() {
        String jobId = UUID.randomUUID().toString();
        JobParameters params = new JobParametersBuilder()
                .addString("JobId", jobId)
                .toJobParameters();

        executor.execute(() -> {
            try {
                LOGGER.info("Submitting Job to run in background with id {}", jobId);
                jobLauncher.run(job, params);
            } catch (JobExecutionAlreadyRunningException
                    | JobRestartException
                    | JobInstanceAlreadyCompleteException
                    | JobParametersInvalidException e) {
                LOGGER.info("Job ran failed for id {}", jobId);
                ResponseEntity.internalServerError().build();
            }
        });

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(jobId)
                .toUri();

        //Send location in response
        return ResponseEntity.created(location).build();
    }

    /*
     * Method to monitor job which triggered ingestion process.
     *
     * curl -X GET -i http://localhost:7171/api/admin/jobs/<id-here>
     */
    @GetMapping("/jobs/{id}")
    public ResponseEntity getIngestJobDetails(@PathVariable String id) {
        LOGGER.info("Try getting job status for id {}", id);
        String sql = "SELECT je.job_execution_id, je.status, je.exit_code, p.string_val " +
                "FROM batch_job_execution je " +
                "INNER JOIN batch_job_execution_params p " +
                "ON je.job_execution_id=p.job_execution_id " +
                "WHERE p.string_val=?";

        Map<String, Object> data = jdbcTemplate.queryForMap(sql, id);

        return ResponseEntity.ok(data);
    }
}
