package com.practice.springbatch.dao;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.practice.springbatch.entity.UserInactive;

@Repository
public interface UserInactiveDao {

  public UserInactive findOne(@Param("userId") String UserId);

  public void add(UserInactive UserInactive);
  
  public void delete(UserInactive UserInactive);

}
