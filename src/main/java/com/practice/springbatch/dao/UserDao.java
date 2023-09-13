package com.practice.springbatch.dao;

import java.util.Date;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.practice.springbatch.entity.User;

@Repository
public interface UserDao {
  
  public User findOne(@Param("userId") String UserId);
  
  public void updateUserState(@Param("state") String state, @Param("lastActionDate") Date lastActionDate,  @Param("id") String id); 

  public void add(User user);
  
  public void delete(User user);
  
}
