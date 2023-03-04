package com.practice.springbatch.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.practice.springbatch.job.CustomJobConfiguration;
import com.practice.springbatch.job.SimpleJobConfiguration;
import com.practice.springbatch.job.StepNextConditionalJobConfiguration;
import com.practice.springbatch.job.StepNextJobConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Scheduler {
  
  @Autowired
  private JobLauncher jobLauncher;
  
  @Autowired
  private SimpleJobConfiguration simpleJobConfiguration;
  
  @Autowired
  private StepNextConditionalJobConfiguration stepNextConditionalJobConfiguration;
  
  @Autowired
  private StepNextJobConfiguration stepNextJobConfiguration;
  
  @Autowired
  private CustomJobConfiguration customJobConfiguration;
  
  
  @Scheduled(cron = "${simpleJob.scheduler}")
  public void runSimpleJob() {
    try {
      JobParameters jobParameters = customJobConfiguration.getJobParmeters();
      jobLauncher.run(simpleJobConfiguration.simpleJob(), jobParameters);
      
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
  
  @Scheduled(cron = "${stepNextConditional.scheduler}")
  public void runStepNextConditionalJob() {
    try {
      JobParameters jobParameters = customJobConfiguration.getJobParmeters();
      jobLauncher.run(stepNextConditionalJobConfiguration.stepNextConditionalJob(), jobParameters);
      
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
  
  @Scheduled(cron = "${stepNextJob.scheduler}")
  public void runStepNextJob() {
    try {
      JobParameters jobParameters = customJobConfiguration.getJobParmeters();
      jobLauncher.run(stepNextJobConfiguration.stepNextJob(), jobParameters);
      
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
  
  
  @Scheduled(cron = "${fileToDBJob.scheduler}")
  public void runFileToDBScheduler() {
    try {
      JobParameters jobParameters = customJobConfiguration.getJobParmeters();
      jobLauncher.run(customJobConfiguration.fileToDBJob(), jobParameters);
      
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
  

  @Scheduled(cron = "${DBToFileJob.scheduler}")
  public void runDBToFileScheduler() {
    try {
      
      JobParameters jobParameters = customJobConfiguration.getJobParmeters();
      jobLauncher.run(customJobConfiguration.DBToFileJob(), jobParameters);
      
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
  
  @Scheduled(cron = "${delete.file.scheduler")
  public void runDeleteFiles() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
    JobParameters jobParameters = customJobConfiguration.getJobParmeters();
    jobLauncher.run(customJobConfiguration.deleteFileJob(), jobParameters);
  }
  
}
