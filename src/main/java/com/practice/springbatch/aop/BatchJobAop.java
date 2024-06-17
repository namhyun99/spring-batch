package com.practice.springbatch.aop;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.practice.springbatch.aop.annotation.BatchProcessing;
import com.practice.springbatch.entity.BatchJob;
import com.practice.springbatch.entity.BatchProcessResult;
import com.practice.springbatch.entity.type.BatchJobType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class BatchJobAop {

  @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
  public void isScheduled() {
    log.info("############ isScheduled()");
  }
  
  @Pointcut("@annotation(com.practice.springbatch.aop.annotation.BatchProcessing)")
  public void isBatchProcessing() {
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

  //@Around("execution(* com.practice.springbatch.process..*(..))")
  @Around("isBatchProcessing()")
  public void batchJobAspect(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("=================================");
    log.info("batchJobAspect...");

    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    log.info("method : " + method);
    
    Object[] args = joinPoint.getArgs(); //메소드에 전달된 파라미터값 가져오기
    
    //호출된 메소드의 어노테이션 값 가져오기
    BatchProcessing batchProcessing = method.getAnnotation(BatchProcessing.class);
    BatchJobType batchJobType = batchProcessing.batchJobType();
    log.info("annotationBatchJobType : " + batchJobType);
    
    BatchJob batchJob = initBatchJob(batchJobType, method);

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

  private BatchJob initBatchJob(BatchJobType batchJobType, Method method) {
    log.info("==> init batchJob. batchJobType={}, Method={} ", new Object[] {batchJobType, method});
    BatchJob batchJob = new BatchJob();
    batchJob.setJobId("TEST_BATCH_JOB");
    batchJob.setBatchType(batchJobType);
   
    return batchJob;
  }
}
