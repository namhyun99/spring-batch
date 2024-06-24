package com.practice.springbatch.process;

import org.springframework.stereotype.Service;

import com.practice.springbatch.aop.annotation.BatchJobWrite;
import com.practice.springbatch.entity.ProcessResult;
import com.practice.springbatch.entity.User;
import com.practice.springbatch.entity.type.BatchJobType;
import com.practice.springbatch.exception.BatchException;
import com.practice.springbatch.exception.BatchResultCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestAspectService {

  @BatchJobWrite(batchJobType = BatchJobType.TEST_BATCH_JOB)
  public ProcessResult testBatchProcessing(String startTime, String endTime, User user) {
    log.info("<== testBatchProcessing..........");
    ProcessResult processResult = new ProcessResult();

    processResult.setReqFileName("TEST");
    processResult.setTotCnt(100);
    processResult.setProcCnt(100);

    throw new BatchException(BatchResultCode.FILE_EXCEPTION, "file not read.");

    //return processResult;
  }
}
