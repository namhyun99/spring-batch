package com.practice.springbatch.aop;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.practice.springbatch.aop.annotation.BatchJobWrite;
import com.practice.springbatch.aop.annotation.CatchException;
import com.practice.springbatch.entity.BatchJob;
import com.practice.springbatch.entity.ProcessResult;
import com.practice.springbatch.entity.type.BatchJobType;
import com.practice.springbatch.exception.BatchException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class BatchJobAop {
  
  @Getter
  private Map<String, BatchJob> batchJobInfoMap = new ConcurrentHashMap<>(); //jobId, Batchjo
  
  

  @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
  public void isScheduled() {}
  
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
  @Pointcut("@annotation(com.practice.springbatch.aop.annotation.CatchException)")
  public void isCatchException() {}

  
  @AfterThrowing(pointcut = "isCatchException()", throwing = "ex")
  public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex) {
    log.error("===> 1. afterThrowingAdvice: " + ex);
    
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();

    //호출된 메소드의 어노테이션 값 가져오기
    CatchException catchException = method.getAnnotation(CatchException.class);
    BatchJobType batchJobType = catchException.batchJobType();
    
    //배치기록.
  }

  @Pointcut("@annotation(com.practice.springbatch.aop.annotation.BatchJobWrite)")
  public void isBatchJobWrite() {}

  //@Around("execution(* com.practice.springbatch.process..*(..))")
  @Around("isBatchJobWrite()")
  public void batchJobAspect(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch time = new StopWatch();
    time.start();


    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    
    //호출된 메소드의 어노테이션 값 가져오기
    BatchJobWrite batchProcessing = method.getAnnotation(BatchJobWrite.class);
    BatchJobType batchJobType = batchProcessing.batchJobType();
    
    String jobId = genJobId(batchJobType);
    ProcessResult processResult = null;
    
    String params = getParameters(joinPoint, signature);
    
    log.info("{} is process started. params [{}]", new Object[] {method.getName(), params});

    try {
      initBatchJob(jobId, batchJobType);

      processResult = (ProcessResult) joinPoint.proceed();
      
      successBatchJob(jobId, processResult);
      
    } catch (BatchException e) {
      log.error("process error.", e);

      failedBatchJob(jobId, processResult, e.getMessage());
    } catch (DataAccessException e) {
      log.error("db Process error.", e);
    
    } finally {
      time.stop();
      
      log.info("{} is process done. elapsed: {} (ms)", new Object[] {method.getName(), time.getTotalTimeMillis()});
    }
  }
  
  private String getParameters(ProceedingJoinPoint joinPoint, MethodSignature signature) {
    Object[] parameterValues = joinPoint.getArgs(); //메소드에 전달된 파라미터값 가져오기
    String[] parameterNames = signature.getParameterNames(); // 메서드의 파라미터들의 이름 배열을 꺼내옵니다. 
    Class<?>[] parameterTypes = signature.getParameterTypes(); // 메서드의 파라미터들의 타입 배열을 꺼내옵니다.
    
    StringBuffer buf = new StringBuffer();
    
    if(parameterValues == null || parameterNames == null) {
      return buf.toString();
    }
    
    for(int i=0; i < parameterNames.length; i++) {
      buf.append(parameterNames[i]);
      buf.append("=");
      buf.append(parameterValues[i]);
      if(i != parameterNames.length-1) {
        buf.append(", ");
      }
    }
    return buf.toString();
  }

  private BatchJob initBatchJob(String jobId, BatchJobType batchJobType) {
    BatchJob batchJob = new BatchJob();
    batchJob.setJobId("TEST_BATCH_JOB");
    batchJob.setBatchType(batchJobType);
   
    batchJobInfoMap.put(jobId, batchJob);
    
    return batchJob;
  }
  
  private void successBatchJob(String jobId, ProcessResult processResult) {
    log.info("==> successBatchJob");
    batchJobInfoMap.remove(jobId);
  }
  
  private void failedBatchJob(String jobId, ProcessResult processResult, String errorMsg) {
    log.info("==> failedBatchJob");
    batchJobInfoMap.remove(jobId);
  }
  
  private String genJobId(BatchJobType batchjobType) {
    String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    
    SecureRandom random = new SecureRandom();
    String randomNum = String.format("%04d", random.nextInt(9999));
    
    StringBuilder builder = new StringBuilder();
    builder.append(batchjobType.getCode());
    builder.append("_");
    builder.append(randomNum);
    builder.append("_");
    builder.append(currentDate);
    
    return builder.toString();
  }
}
