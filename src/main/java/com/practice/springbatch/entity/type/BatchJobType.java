package com.practice.springbatch.entity.type;

import lombok.Getter;

public enum BatchJobType {
  TEST_BATCH_JOB("TP"),
  ;
  
  @Getter
  private String code;
  
  BatchJobType(String code){ this.code = code; }

}
