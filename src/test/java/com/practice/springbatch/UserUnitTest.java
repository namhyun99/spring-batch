package com.practice.springbatch;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("유저데이터_적재")
  public void insertUser() {
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
  @DisplayName("분할_저장_테스트")
  public void splitSave() {
    
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
  @DisplayName("시간_조건_테스트")
  public void timeCondition() {
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
  
  @Before
  public void setUp() {
    User user = new User();
    user.setId("TEST_ID");
    user.setName("테스트계쩡");
    user.setState(UserState.Y);
    user.setLastActionDate(new Date());
    userService.add(user);
  }
  
  @Test
  @DisplayName("유저조회시_락_테스트")
  public void selectForLock() throws InterruptedException {
    int numOfThread = 2;
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    CountDownLatch countDownLatch = new CountDownLatch(numOfThread);
    
    for(int i=0; i<numOfThread; i++) {
      executorService.execute(() -> {
        try {
          User user = userService.findOne("TEST_ID");
          log.info("user: " + user);
        } catch (Exception e) {
          e.printStackTrace();
        }
        countDownLatch.countDown();
      });
    }
    countDownLatch.await();
    
  }
  
}
