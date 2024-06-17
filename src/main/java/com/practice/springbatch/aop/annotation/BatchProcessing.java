package com.practice.springbatch.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.practice.springbatch.entity.type.BatchJobType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BatchProcessing {
  BatchJobType batchJobType();  //default BatchJobType.TEST_BATCH_JOB;
}
