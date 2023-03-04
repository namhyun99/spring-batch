package com.practice.springbatch.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.practice.springbatch.dto.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class RedisController {
  
//  @Autowired
//  private RedisConnectionService redisConnectionService;
  
  @Autowired
  private RedisCacheService redisCacheService;
  
//  @PostMapping(value = "/getRedisStringValue")
//  @ResponseStatus(HttpStatus.OK)
//  public void getRedisStringValue(String key) {
//    redisConnectionService.getRedisStaringvalue(key);
//  }
  
  @PostMapping(value = "/add")
  @ResponseStatus(HttpStatus.OK)
  public Order createOrder(@RequestBody Order order) {
    return redisCacheService.createOrder(order);
  }
  
  @GetMapping(value = "/test")
  @ResponseStatus(HttpStatus.OK)
  public Order getMember(@RequestParam Integer orderNo) {
    log.info("orderNo : " + orderNo);
    return redisCacheService.getOrder(orderNo);
  }
  
  
  
  
}
