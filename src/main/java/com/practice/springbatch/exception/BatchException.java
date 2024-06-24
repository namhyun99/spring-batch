package com.practice.springbatch.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;

  private String            code;
  private String            message;

  public BatchException(BatchResultCode batchResultCode) {
    this(batchResultCode, null);
  }

  public BatchException(BatchResultCode batchResultCode, String message) {
    this.code = batchResultCode.getCode();
    this.message = message;
  }

}
