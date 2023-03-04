package com.practice.springbatch.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.practice.springbatch.entity.FileDeleteDataResult;
import com.practice.springbatch.entity.data.FileDeleteData;
import com.practice.springbatch.entity.type.FileType;
import com.practice.springbatch.entity.type.group.FileGroup;
import com.practice.springbatch.service.file.FileWriterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor  //생성자 DI를 위한 lombok 어노테이션
@Configuration  //스프링부트의 모든 Job은 해당 어노테이션을 사용
public class CustomJobConfiguration {
  
  private final JobBuilderFactory jobBuilderFactory; //생성자 DI받음
  private final StepBuilderFactory stepBuilderFactory; //생성자 DI받음
  
  @Autowired
  FileWriterService fileWriterService;
  
  //1. FILE TO DB
  @Bean
  public Job fileToDBJob() {
    return jobBuilderFactory.get("fileToDBJob")
        .start(stepFileToDB(null))
        .build();
  }
  
  //2. DB TO FILE
  @Bean
  public Job DBToFileJob() {
    return jobBuilderFactory.get("DBToFileJob")
        .start(stepDBToFile(null))
        .build();
  }
  
  //3. Delete File
  public Job deleteFileJob() {
    return jobBuilderFactory.get("deleteFileJob")
        .start(stepDeleteFiles(null))
        .build();
  }


  //========== Step Start ===========//
  
  @Bean
  @JobScope
  public Step stepDeleteFiles(@Value("#{jobParameters[time]}") Date time) {
    return stepBuilderFactory.get("stepDeleteFiles").tasklet((contribution, chunkContext) -> {
      log.info(">>>>>> This is step delete files");
      
      
      List<FileGroup> fileGroupList = FileGroup.findByFileType2(FileType.BASIC);
      List<FileDeleteData> fileDeleteDataList = new ArrayList<>();
      try {
        //1 배치 최초 Insert
        
        
        for(FileGroup fileGroup : fileGroupList) {
          
//          fileWriterService.deleteFiles(fileGroup);
          FileDeleteData oriFileDirData = new FileDeleteData();
          oriFileDirData.setOriginalFileDir(fileGroup.getFileDir());
          oriFileDirData.setDeleteFileDir(fileGroup.getFileDir());
          oriFileDirData.setRemainCount(2);
          FileDeleteDataResult oriFileDirDataResult = 
              fileWriterService.deleteFiles(oriFileDirData);

          FileDeleteData bakFileDirData = new FileDeleteData();
          bakFileDirData.setOriginalFileDir(fileGroup.getFileDir());
          bakFileDirData.setDeleteFileDir(fileGroup.getFileDir()+"/bak");
          bakFileDirData.setRemainCount(2);
          FileDeleteDataResult bakFileDirDataResult = 
              fileWriterService.deleteFiles(bakFileDirData);
          
          FileDeleteData fileDeleteData = new FileDeleteData();
          fileDeleteData.setFileId(fileGroup.name());
          fileDeleteData.setFileOriDirDataResult(oriFileDirDataResult);
          fileDeleteData.setFileBakDirDataResult(bakFileDirDataResult);
          fileDeleteDataList.add(fileDeleteData);
        }
        
        
        
      } catch (Exception e) {
        // TODO: handle exception
      }
      
      
      
      
      return RepeatStatus.FINISHED;
    }).build();
  }

  @Bean
  @JobScope
  public Step stepFileToDB(@Value("#{jobParameters[time]}") Date time) {
    return stepBuilderFactory.get("stepFileToDB")
        .tasklet((contribution, chunkContext) -> {
          log.debug(">>>>>>>>> This is step File to DB");
          log.debug(">>>>>>>>> requestDate = {}", time);
          
          //1. 
          
          return RepeatStatus.FINISHED;
        }).build();
  }
  

  @Bean
  @JobScope
  public Step stepDBToFile(@Value("#{jobParameters[time]}") Date time) {
    return stepBuilderFactory.get("stepDBToFile")
        .tasklet((contribution, chunkContext) -> {
          log.info(">>>>>>>>> This is step DB to File");
          log.info(">>>>>>>>> requestDate = {}", time);
          
          //1. 
          
          return RepeatStatus.FINISHED;
        }).build();
  }
  
  
  public JobParameters getJobParmeters() {
    Map<String, JobParameter> confMap = new HashMap<>();
    confMap.put("time", new JobParameter(new Date()));
    return new JobParameters(confMap);
  }
}
