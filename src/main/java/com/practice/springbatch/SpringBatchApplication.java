package com.practice.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling //스케줄링 기능 활성화
@EnableBatchProcessing  //배치기능 활성화
@EnableCaching // 캐시기능 활성화
@SpringBootApplication(scanBasePackages = {"com.practice.springbatch"})
@ImportResource("classpath:./META-INF/spring/context-*.xml")
public class SpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
