package com.practice.springbatch.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BatchController {
  
//  @GetMapping(value = "/enumTest")
//  @ResponseStatus(HttpStatus.OK)
//  public FileInfoGroup enumTest() throws IllegalAccessException {
//    FileType fileType = FileType.BASIC;
//    FileInfoGroup fileGroup = FileInfoGroup.findByFileType(fileType);
//    
//    log.info("FileId: [{}], FileDir: [{}], FileType: [{}], fileStatus: [{}]", 
//        fileGroup.name(), fileGroup.getFileDir(), fileGroup.getFileType(), fileGroup.getFileStatusType());
//    
//    return FileInfoGroup.findByFileType(fileType);
//  }
}
