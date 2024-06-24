package com.practice.springbatch.process;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.springbatch.aop.annotation.CatchException;
import com.practice.springbatch.entity.User;
import com.practice.springbatch.entity.type.BatchJobType;
import com.practice.springbatch.entity.type.UserState;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestAspectProcessService {
  
  @Autowired
  private TestAspectService testAspectService;

  @CatchException(batchJobType = BatchJobType.TEST_BATCH_JOB)
  public void doProcess() throws Exception {
    log.info("==> doProcess()");
    String startTime = "20240524";
    String endTime = "20240624";
    
    User user = new User("testId", "testName",UserState.N, new Date());
    
    
    testAspectService.testBatchProcessing(startTime, endTime, user); 
    //throw new Exception("Exception Test");
  }
}
