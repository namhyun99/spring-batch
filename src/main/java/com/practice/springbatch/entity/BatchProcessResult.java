package com.practice.springbatch.entity;

import org.springframework.batch.core.BatchStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchProcessResult {
  private String      reqFileName;
  private Integer     reqFileSize;
  private String      resFileName;
  private Integer     resFileSize;
  private Integer     totCnt;
  private Integer     procCnt;
  private Integer     unProcCnt;
  private BatchStatus status;
  private String      resultCode;
  private String      resultMsg;

}
