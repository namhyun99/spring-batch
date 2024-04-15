package com.practice.springbatch.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.springbatch.dao.BatchJobDao;
import com.practice.springbatch.entity.BatchJob;
import com.practice.springbatch.enums.BatchType;

import io.lettuce.core.dynamic.batch.BatchException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BatchJobService {
  
  @Autowired
  private BatchJobDao batchJobDao;

  
  public void add(BatchJob batchJob) {
    log.info("insert: "+ batchJob);
    if(batchJob.getJobId() == null) {
      return;
    }
    batchJobDao.insert(batchJob);
  }
  
  public void update(BatchJob batchJob) {
    log.info("update: " + batchJob);
    batchJobDao.update(batchJob);
  }
  
  
  public JobParameters getJobParmeters() {
    Map<String, JobParameter> confMap = new HashMap<>();
    confMap.put("time", new JobParameter(new Date()));
    return new JobParameters(confMap);
  }
  
  public JobParameters getJobParmeters(String batchId, BatchType batchType) {
    Date bizDate = new Date();
    String jobId = createJobId(bizDate, batchId, batchType);
    
    Map<String, JobParameter> confMap = new HashMap<>();
    confMap.put("time", new JobParameter(bizDate));
    confMap.put("batchId", new JobParameter(batchId));
    confMap.put("batchType", new JobParameter(batchType.name()));
    confMap.put("jobId", new JobParameter(jobId));
    
    return new JobParameters(confMap);
  }


  public String createJobId(Date bizDate, String batchId, BatchType type) throws BatchException {

    Random random = new Random();
    String randomNum = String.format("%04d", random.nextInt(9999));

    SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
    String bizDateStr = sdf.format(bizDate);

    String jobId = "";

    StringBuffer sb = new StringBuffer();
    switch (type) {
      case PAYMENT_HISTORY:
        sb.append(batchId.toUpperCase());
        sb.append("_");
        sb.append(randomNum);
        sb.append("_");
        sb.append(bizDateStr);
        jobId = sb.toString();
        break;
      case OPERATION_INFO:
        break;
    }
    
    log.info(">>>>>>>>>>>>>>> craeted jobId : " + jobId);

    return jobId;
  }
  

  
  

}
