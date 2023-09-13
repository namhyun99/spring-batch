package com.practice.springbatch;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.practice.springbatch.entity.User;
import com.practice.springbatch.entity.UserActive;
import com.practice.springbatch.entity.type.UserState;
import com.practice.springbatch.service.UserActiveService;
import com.practice.springbatch.service.UserInactiveService;
import com.practice.springbatch.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserUnitTest {
  
  @Autowired
  private UserService userService;
  @Autowired
  private UserActiveService userActiveService;
  @Autowired
  private UserInactiveService userInactiveService;
  
  @Test
  @Transactional
  public void 유저데이터_적재() {
    try {
      int size = 10;
      
      for(int i=0; i < size; i++) {
        User user = new User();
        user.setId("kim" + i);
        user.setName("김아무개" + i);
        user.setState(UserState.N);
        user.setLastActionDate(new Date());
        
        userService.add(user);
        
        UserActive userActive = new UserActive();
        userActive.setId(user.getId());
        userActive.setName(user.getName());
        
        userActiveService.add(userActive);
      }
      
      for(int i=0; i < size; i++) {
        User user = new User();
        user.setId("park" + i);
        user.setName("박아무개" + i);
        user.setState(UserState.N);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH , -1);
        user.setLastActionDate(cal.getTime());

        userService.add(user);
        
        UserActive userActive = new UserActive();
        userActive.setId(user.getId());
        userActive.setName(user.getName());
        
        userActiveService.add(userActive);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
}
