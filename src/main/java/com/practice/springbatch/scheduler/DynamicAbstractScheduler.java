package com.practice.springbatch.scheduler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public abstract class DynamicAbstractScheduler {
  private ThreadPoolTaskScheduler scheduler;
  private Map<String,ThreadPoolTaskScheduler> schedulerMap = new HashMap<>();
  
  @PostConstruct
  public void init() {
    startScheduler();
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
    scheduler = new ThreadPoolTaskScheduler();
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
