package com.practice.springbatch.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.practice.springbatch.entity.User;
import com.practice.springbatch.entity.type.UserState;

@Repository
public interface UserDao {
  
  public List<User> findAll();
  
  public List<User> findByRestMonthAgo();

  public User findOne(@Param("id") String UserId);
  
  public void updateUserState(@Param("state") UserState state, @Param("lastActionDate") Date lastActionDate,  @Param("id") String id); 

  public void add(User user);

  public void update(User user);
  
  public void delete(User user);

  
}
