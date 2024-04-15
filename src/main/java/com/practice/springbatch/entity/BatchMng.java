package com.practice.springbatch.entity;

import lombok.Data;

@Data
public class BatchMng {
  private int id;   //pk
  private String intfId; //pk
  private String filePath; 
  private String fileName; 
  private String cronTimeSecound;
  private String cronTimeMinute;
  private String cronTimeHour;
  private String cronTimeDay;
  private String cronTimeWeek;
  private String cronTimeYear;
}
