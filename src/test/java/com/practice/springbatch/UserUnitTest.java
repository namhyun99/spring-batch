package com.practice.springbatch;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.practice.springbatch.entity.User;
import com.practice.springbatch.entity.UserActive;
import com.practice.springbatch.entity.UserInactive;
import com.practice.springbatch.entity.type.UserState;
import com.practice.springbatch.service.UserActiveService;
import com.practice.springbatch.service.UserInactiveService;
import com.practice.springbatch.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
  
  
  @Test
  @Transactional
  public void 분할_저장_테스트() {
    
    List<User> userList = userService.findByRestMonthAgo();
    log.info("userListSize: " + userList.size());

    int count = 0;
    int limitCount = 10;
    
    while(count < userList.size()) {
      log.info("count={}, limitCount={}", count, limitCount);
      List<User> chunkUserList = userList.stream()
                                         .skip(count)
                                         .limit(limitCount)
                                         .collect(Collectors.toList());
      log.info("chunkUserListSize: " + chunkUserList.size());
      for(User user : chunkUserList) {
        
        UserInactive userInaction = new UserInactive();
        userInaction.setId(user.getId());
        userInaction.setName(user.getName());
        userInactiveService.add(userInaction);
        
        UserActive userActive = new UserActive();
        userActive.setId(user.getId());
        userActive.setName(user.getName());
        userActiveService.delete(userActive);
        
        user.setState(UserState.Y);
        user.setLastActionDate(new Date());
        userService.updateState(user.getState(), user.getLastActionDate(), user.getId());
      }
      
      count += limitCount;
    }
  }
  
  @Test
  public void 시간_조건_테스트() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asis/Seoul"));
    LocalDateTime nowDate = LocalDateTime.now();
    log.info("nowDate : " + nowDate);
    
    LocalDateTime endDate = nowDate.plusSeconds(1);
//    LocalDateTime endDate = nowDate.plusMinutes(1);
//    LocalDateTime endDate = nowDate.plusHours(1);
    log.info("endDate: " + endDate);
    
    int max_count = 10000000;
    int count = 0;
    LocalDateTime processDate = nowDate;
    while(count < max_count) {
      
      if(processDate.isAfter(endDate)) {
        log.info("TimeOver. processDate={}, endDate={}", processDate, endDate);
        break;
      }
      
      log.info(count + "프로세스 처리 및 종료....");
      count++;
      processDate = LocalDateTime.now();
    }
  }
  
}
