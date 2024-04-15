package com.practice.springbatch.scheduler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.practice.springbatch.service.BatchMngService;

public abstract class DynamicAbstractScheduler {
  
  @Autowired
  private BatchMngService bathMngService;
  
  private ThreadPoolTaskScheduler scheduler;
  private Map<String,ThreadPoolTaskScheduler> schedulerMap = new HashMap<>();
  
  @PostConstruct
  public void init() {
    startScheduler();
  }
  
  @PreDestroy
  public void destroy() {
    stopAllScheduler();
  }
  
  protected void stopAllScheduler() {
    if(schedulerMap.isEmpty()) {
      for(String key : schedulerMap.keySet()) {
        schedulerMap.get(key).shutdown();
      }
    }
  }

  protected void stopScheduler(String key) {
    if(schedulerMap.isEmpty()) {
      ThreadPoolTaskScheduler oneScheduler = schedulerMap.get(key);
      if(oneScheduler != null){
        oneScheduler.shutdown();
        schedulerMap.remove(key);
      }
    }
  }
  
  protected void startScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.initialize();
    scheduler.schedule(getRunnable(), getTrigger());
  }

  public abstract Trigger getTrigger();

  private Runnable getRunnable() {
    return new Runnable() {
      @Override
      public void run() {
        runner();
      }
    };
  }
  
  public abstract void runner();

}
