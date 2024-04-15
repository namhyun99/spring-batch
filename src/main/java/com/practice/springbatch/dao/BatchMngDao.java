package com.practice.springbatch.dao;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.practice.springbatch.entity.BatchMng;

@Repository
public interface BatchMngDao {
  
  public BatchMng findByInterId(@Param("interId") String interId);

}
