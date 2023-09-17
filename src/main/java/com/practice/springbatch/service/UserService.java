package com.practice.springbatch.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.springbatch.dao.UserDao;
import com.practice.springbatch.entity.User;
import com.practice.springbatch.entity.type.UserState;

@Service
public class UserService {
  
  @Autowired
  private UserDao userDao;
  
  public List<User> findAll(){
    return userDao.findAll();
  }
  
  public List<User> findByRestMonthAgo(){
    return userDao.findByRestMonthAgo();
  }
  
  public User findOne (String UserId){
    return userDao.findOne(UserId);
  }
  
  public void updateState(UserState state, Date lastActionDate, String id) {
    userDao.updateUserState(state, lastActionDate, id);
  }

  public void add(User user) {
    userDao.add(user);
  }
  
  public void delete(User user) {
    userDao.delete(user);
  }

}
