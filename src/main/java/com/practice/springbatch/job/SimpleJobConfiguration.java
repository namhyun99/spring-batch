package com.practice.springbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.practice.springbatch.entity.type.BatchJobType;
import com.practice.springbatch.process.TestAspectService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor  //생성자 DI를 위한 lombok 어노테이션
@Configuration  //스프링부트의 모든 Job은 해당 어노테이션을 사용
public class SimpleJobConfiguration {
  
  private final JobBuilderFactory jobBuilderFactory; //생성자 DI받음
  private final StepBuilderFactory stepBuilderFactory; //생성자 DI받음
  
  @Autowired
  private TestAspectService testAspectService;
  
  @Bean
  public Job simpleJob() {
    return jobBuilderFactory.get("simpleJob") // simpleJob이라는 BatchJob을 생성, job의 이름은 별도로 지정하지 않고, 아래와 같이 Builder를 통해 지정함
        .start(simpleStep1())
        .build();
  }
  
  
  @Bean
  @JobScope
  public Step simpleStep1() {
    return stepBuilderFactory.get("simpleStep1") //simpleStep이라는 BatchStep을 생성. Builder를 통해 이름을 지정
        .tasklet((contribution, chunkContext) -> { //step안에 수행될 기능을 명시, tasklet은 Step안에서 단일로 수행될 커스텀한 기능을 선언할 때 사용.
          log.info(">>>>>> This is Step1"); //배치가 수행되면 로그 출력
          
          for(int i=0; i<5; i++) {
            testAspectService.testBatchProcessing();
          }
          
          return RepeatStatus.FINISHED;
        })
        .build();
  }
}
