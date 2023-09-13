package com.practice.springbatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.springbatch.dao.UserActiveDao;
import com.practice.springbatch.entity.UserActive;

@Service
public class UserActiveService {
  
  @Autowired
  private UserActiveDao userActiveDao;
  
  public UserActive findOne (String UserId){
    return userActiveDao.findOne(UserId);
  }

  public void add(UserActive userActive) {
    userActiveDao.add(userActive);
  }
  
  public void delete(UserActive userActive) {
    userActiveDao.delete(userActive);
  }
}
