package com.practice.springbatch.job;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration  //스프링부트의 모든 Job은 해당 어노테이션을 사용
public class CustomJobConfiguration extends CommonBatchJobStep {
  
  public CustomJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
    super(jobBuilderFactory, stepBuilderFactory);
  }


  @Bean
  public Job jobTest001() {
    return jobBuilderFactory.get("jobTest001")
        .start(initBatchJobStep(null, null)).on("FAILED").to(failedBatchJobStep(null)).on("*").end()
        .from(initBatchJobStep(null, null)).on("*").to(stepPaymentHistory(null)).on("FAILED").to(failedBatchJobStep(null)).on("*").end()
        .from(stepPaymentHistory(null)).on("*").to(successfulBatchJobStep(null)).end()
        .build();
  }


  //========== Step Start ===========//
  
  @Bean
  @JobScope
  public Step stepPaymentHistory(@Value("#{jobParameters[jobId]}") String jobId) {
    return stepBuilderFactory.get("stepPaymentHistory").tasklet((contribution, chunkContext) -> {
      log.info(">>>>>> This is step PaymentHistory");
      
      if(jobId == null) {
        return RepeatStatus.FINISHED;
      }
        
      try {
          String reqFileName = "TEST_" + jobId + "_001.B";
          Integer fileSize = 132023;
          
//          throw new Exception("Custom Exception");
          batchJobMap.get(jobId).setReqFileName(reqFileName);
          batchJobMap.get(jobId).setReqFileSize(fileSize);
          batchJobMap.get(jobId).setTotCnt(32);
          batchJobMap.get(jobId).setProcCnt(32);
          batchJobMap.get(jobId).setUnProcCnt(0);
          contribution.setExitStatus(ExitStatus.COMPLETED);
      } catch (Exception e) {
        e.printStackTrace();
        batchJobMap.get(jobId).setResultMsg(e.getMessage());
        contribution.setExitStatus(ExitStatus.FAILED);
      }
        
      return RepeatStatus.FINISHED;
    }).build();
  }


  

}
