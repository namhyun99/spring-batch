package com.practice.springbatch.entity;


import java.util.Date;

import org.springframework.batch.core.BatchStatus;

import lombok.Data;

@Data
public class BatchJob {

  private String      jobId;
  private String      jobName;
  private String      batchType;
  private Date        startDate;
  private Date        endDate;
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
