package com.practice.springbatch.process;

import org.springframework.stereotype.Service;

import com.practice.springbatch.entity.BatchProcessResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestAspectService {

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
