package com.practice.springbatch.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.springbatch.dao.UserDao;
import com.practice.springbatch.entity.User;

@Service
public class UserService {
  
  @Autowired
  private UserDao userDao;
  
  public User findOne (String UserId){
    return userDao.findOne(UserId);
  }
  
  public void updateState(String state, Date lastActionDate,  String id) {
    userDao.updateUserState(state, lastActionDate, id);
  }

  public void add(User user) {
    userDao.add(user);
  }
  
  public void delete(User user) {
    userDao.delete(user);
  }

}
