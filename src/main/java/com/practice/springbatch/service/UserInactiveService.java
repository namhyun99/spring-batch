package com.practice.springbatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.springbatch.dao.UserInactiveDao;
import com.practice.springbatch.entity.UserInactive;

@Service
public class UserInactiveService {
  
  @Autowired
  private UserInactiveDao userInactiveDao;
  
  public UserInactive findOne (String UserId){
    return userInactiveDao.findOne(UserId);
  }

  public void add(UserInactive userInactive) {
    userInactiveDao.add(userInactive);
  }
  
  public void delete(UserInactive userInactive) {
    userInactiveDao.delete(userInactive);
  }
}
