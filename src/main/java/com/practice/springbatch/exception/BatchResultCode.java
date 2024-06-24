package com.practice.springbatch.exception;

import lombok.Getter;

public enum BatchResultCode {
  FILE_EXCEPTION("1"),
  READ_EXCEPTION("2"),
  WRITE_EXCEPTION("3"),;

  @Getter
  private String code;

  private BatchResultCode(String code) {
    this.code = code;
  }
  
  public BatchResultCode find(String code) {
    for(BatchResultCode resultCode : BatchResultCode.values()) {
      if(resultCode.getCode().equals(code)) {
        return resultCode;
      }
    }
    return null;
  }
}
