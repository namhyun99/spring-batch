package com.practice.springbatch.dao;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.practice.springbatch.entity.UserActive;
import com.practice.springbatch.entity.UserInactive;

@Repository
public interface UserActiveDao {

  public UserActive findOne(@Param("userId") String UserId);

  public void add(UserActive userActive);
  
  public void delete(UserActive userActive);

}
