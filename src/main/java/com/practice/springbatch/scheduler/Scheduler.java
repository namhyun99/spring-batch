package com.practice.springbatch.scheduler;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.practice.springbatch.entity.BatchMng;
import com.practice.springbatch.enums.BatchType;
import com.practice.springbatch.job.CustomJobConfiguration;
import com.practice.springbatch.job.SimpleJobConfiguration;
import com.practice.springbatch.job.StepNextConditionalJobConfiguration;
import com.practice.springbatch.job.StepNextJobConfiguration;
import com.practice.springbatch.service.BatchJobService;
import com.practice.springbatch.service.BatchMngService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Scheduler extends DynamicAbstractScheduler {
  
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
  @Autowired
  private BatchJobService batchJobService;
  @Autowired
  private BatchMngService batchMngService;
  
  @Value("${scheduler.paymenthistory}")
  private String paymentHistoryCron;
  
  @Scheduled(cron = "${simpleJob.scheduler}")
  public void runSimpleJob() {
    try {
      JobParameters jobParameters = batchJobService.getJobParmeters();
      jobLauncher.run(simpleJobConfiguration.simpleJob(), jobParameters);
      
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
  
  @Scheduled(cron = "${stepNextConditional.scheduler}")
  public void runStepNextConditionalJob() {
    try {
      JobParameters jobParameters = batchJobService.getJobParmeters();
      jobLauncher.run(stepNextConditionalJobConfiguration.stepNextConditionalJob(), jobParameters);
      
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
  
  @Scheduled(cron = "${stepNextJob.scheduler}")
  public void runStepNextJob() {
    try {
      JobParameters jobParameters = batchJobService.getJobParmeters();
      jobLauncher.run(stepNextJobConfiguration.stepNextJob(), jobParameters);
      
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
  
  @Scheduled(cron = "${scheduler.paymenthistory}")
  public void runPaymentHistory() {
    try {
      JobParameters jobTest001Params = batchJobService.getJobParmeters("jobTest001", BatchType.PAYMENT_HISTORY);
      jobLauncher.run(customJobConfiguration.jobTest001(), jobTest001Params);
      
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public Trigger getTrigger() {
    //BatchMng batchMng = batchMngService.findByInterId("INT-B-0001");
    BatchMng batchMng = new BatchMng("0", "30", "18", "*", "*", "*");
    log.info("batchMng: " + batchMng);
    String paymentHistoryCron = 
        batchMng.getCronTimeSecound() + " " 
        + batchMng.getCronTimeMinute() + " "
        + batchMng.getCronTimeHour() + " "
        + batchMng.getCronTimeDay() + " "
        + batchMng.getCronTimeWeek() + " "
        + batchMng.getCronTimeYear();
    log.info("paymentHistoryCron={} ",paymentHistoryCron);
    return new CronTrigger(paymentHistoryCron);
  }
  @Override
  public void runner() {
    JobParameters jobTest001Params = batchJobService.getJobParmeters("jobTest001", BatchType.PAYMENT_HISTORY);
    log.info(">>>>>>>>>>>RNN");
    try {
      jobLauncher.run(customJobConfiguration.jobTest001(), jobTest001Params);
    } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
      e.printStackTrace();
    }
  }
  
  
}
