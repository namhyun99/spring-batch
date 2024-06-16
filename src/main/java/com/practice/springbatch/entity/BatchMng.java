package com.practice.springbatch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
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
  
  public BatchMng(String cronTimeSecound, String cronTimeMinute, String cronTimeHour, String cronTimeDay, String cronTimeWeek, String cronTimeYear) {
    super();
    this.cronTimeSecound = cronTimeSecound;
    this.cronTimeMinute = cronTimeMinute;
    this.cronTimeHour = cronTimeHour;
    this.cronTimeDay = cronTimeDay;
    this.cronTimeWeek = cronTimeWeek;
    this.cronTimeYear = cronTimeYear;
  }
  
  
}
