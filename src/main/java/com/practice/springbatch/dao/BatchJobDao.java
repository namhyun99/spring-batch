package com.practice.springbatch.dao;

import org.springframework.stereotype.Repository;

import com.practice.springbatch.entity.BatchJob;

@Repository
public interface BatchJobDao {
  
  public void insert(BatchJob batchJob);
  
  public void update(BatchJob batchJob);
  
}
