package com.practice.springbatch.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.practice.springbatch.entity.BatchJob;
import com.practice.springbatch.entity.type.BatchJobType;
import com.practice.springbatch.service.BatchJobService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public abstract class CommonBatchJobStep {

  protected final JobBuilderFactory  jobBuilderFactory;  //생성자 DI받음
  protected final StepBuilderFactory stepBuilderFactory; //생성자 DI받음

  @Getter
  protected Map<String, BatchJob> batchJobMap = new HashMap<>();

  @Autowired
  public BatchJobService batchJobService;

  @Bean
  @JobScope
  public Step initBatchJobStep(@Value("#{jobParameters[jobId]}") String jobId, @Value("#{jobParameters[batchType]}") String batchType) {
    return stepBuilderFactory.get("initBatchJobStep").tasklet((contribution, chunkContext) -> {
      log.info(">>>>>> This is step init batch job");
      log.info(">>>>>> Received Parameters: jobId: {}, batchType: {}", new Object[] {jobId, batchType});
      if (jobId == null || batchType == null) {
        return RepeatStatus.FINISHED;
      }

      try {
        log.info("init batch map size: " + batchJobMap.size());
        String jobName = contribution.getStepExecution().getJobExecution().getJobInstance().getJobName();

        BatchJob batchJob = new BatchJob();
        batchJob.setJobId(jobId);
        batchJob.setJobName(jobName);
        batchJob.setBatchType(BatchJobType.TEST_BATCH_JOB);
        batchJob.setStartDate(new Date());
        batchJob.setEndDate(new Date());
        batchJob.setStatus(BatchStatus.STARTED);
        batchJobMap.put(jobId, batchJob);

        log.info("Init batch map size: " + batchJob);
        batchJobService.add(batchJobMap.get(jobId));
      } catch (Exception e) {
        e.printStackTrace();
        contribution.setExitStatus(ExitStatus.FAILED);
        // TODO: handle exception
      }
      return RepeatStatus.FINISHED;
    }).build();
  }

  @Bean
  @JobScope
  public Step successfulBatchJobStep(@Value("#{jobParameters[jobId]}") String jobId) {
    return stepBuilderFactory.get("successfulBatchJobStep").tasklet((contribution, chunkContext) -> {
      log.info(">>>>>> This is step Successful Batch job");

      if (jobId == null) {
        return RepeatStatus.FINISHED;
      }

      Date endDate = new Date();
      batchJobMap.get(jobId).setEndDate(endDate);
      batchJobMap.get(jobId).setStatus(BatchStatus.COMPLETED);
      batchJobMap.get(jobId).setResultCode("9000");
      batchJobMap.get(jobId).setResultMsg("successful batch job");

      batchJobService.update(batchJobMap.get(jobId));
      log.info("Success batch map size: " + batchJobMap.size());
      removeBatchJobMapByJobId(jobId);
      log.info("Remove batch map size: " + batchJobMap.size());
      return RepeatStatus.FINISHED;
    }).build();
  }

  @Bean
  @JobScope
  public Step failedBatchJobStep(@Value("#{jobParameters[jobId]}") String jobId) {
    return stepBuilderFactory.get("failedBatchJobStep").tasklet((contribution, chunkContext) -> {
      log.info(">>>>>> This is step step Failed Batch Job ");

      if (jobId == null) {
        return RepeatStatus.FINISHED;
      }

      Date endDate = new Date();
      batchJobMap.get(jobId).setEndDate(endDate);
      batchJobMap.get(jobId).setStatus(BatchStatus.FAILED);
      batchJobMap.get(jobId).setResultCode("1000");
      batchJobMap.get(jobId).setResultMsg("falied batch job");

      batchJobService.update(batchJobMap.get(jobId));
      removeBatchJobMapByJobId(jobId);
      return RepeatStatus.FINISHED;
    }).build();
  }

  public BatchJob getBatchJobByJobId(String jobId) {
    for (String key : batchJobMap.keySet()) {
      if (key.equals(jobId)) {
        return batchJobMap.get(key);
      }
    }
    return null;
  }

  public void removeBatchJobMapByJobId(String jobId) {
    batchJobMap.remove(jobId);
  }
}
