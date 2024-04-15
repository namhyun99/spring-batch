package com.practice.springbatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.springbatch.dao.BatchMngDao;
import com.practice.springbatch.entity.BatchMng;

@Service
public class BatchMngService {

  @Autowired
  private BatchMngDao batchMngDao;
  
  public BatchMng findByInterId(String interId) {
    return batchMngDao.findByInterId(interId);
  }
}
