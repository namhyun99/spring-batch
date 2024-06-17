package com.practice.springbatch.process;

import org.springframework.stereotype.Service;

import com.practice.springbatch.aop.annotation.BatchProcessing;
import com.practice.springbatch.entity.BatchProcessResult;
import com.practice.springbatch.entity.type.BatchJobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestAspectService {

  @BatchProcessing(batchJobType = BatchJobType.TEST_BATCH_JOB)
  public BatchProcessResult testBatchProcessing() throws Exception {
    log.info("<== testBatchProcessing..........");
    BatchProcessResult processResult = new BatchProcessResult();

    processResult.setReqFileName("TEST");
    processResult.setTotCnt(100);
    processResult.setProcCnt(100);

    //throw new Exception("test Exception");

    return processResult;
  }
}
