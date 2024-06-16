package com.practice.springbatch.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.practice.springbatch.entity.BatchJob;
import com.practice.springbatch.entity.BatchProcessResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class BatchJobAop {

  @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
  public void isScheduled() {
    log.info("############ isScheduled()");
  }

  @Pointcut("@annotation(org.springframework.batch.core.configuration.annotation.JobScope)")
  public void isJobScope() {
    log.info("isJobScope");
  }

  @Around("isScheduled()")
  public Object scheduledAspect(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("scheduledAspect");
    return joinPoint.proceed();
  }

  /*
  @Before("execution(* com.practice.springbatch.job..*(..))")
  public void before() {
    log.info("before aspect");
  }

  @After("execution(* com.practice.springbatch.job..*(..))")
  public void after() {
    log.info("after aspect");
  }
  */

  @Around("execution(* com.practice.springbatch.process..*(..))")
  //  @Around("isJobScope()")
  public void batchJobAspect(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("=================================");
    log.info("batchJobAspect...");

    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    log.info("method : " + method);
    
    BatchJob batchJob = initBatchJob("Test", method);

    try {
      BatchProcessResult batchProcessResult = (BatchProcessResult) joinPoint.proceed();
      log.info("result: " + batchProcessResult);
      
      log.info("==> Success BatchJob ");
    } catch (Exception e) {

      batchJob.setResultMsg(e.getMessage());
      log.info("result: " + batchJob);
      log.error("==> failed BatchJob");
      throw e;

    } finally {
      log.info("=================================");
    }
  }

  private BatchJob initBatchJob(String BatchJobType, Method method) {
    log.info("==> init batchJob. batchJobType={}, Method={} ", new Object[] {BatchJobType, method});
    BatchJob batchJob = new BatchJob();
    batchJob.setJobId("TEST_BATCH_JOB");
    batchJob.setBatchType("TEST_BATCH_TYPE");
   
    return batchJob;
  }
}
