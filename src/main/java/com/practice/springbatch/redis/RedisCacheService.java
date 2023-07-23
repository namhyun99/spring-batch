package com.practice.springbatch.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.practice.springbatch.dao.OrderDao;
import com.practice.springbatch.entity.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisCacheService {
  
  @Autowired
  private OrderDao orderDao;
  
  public Order createOrder(Order order) {
    return orderDao.createOrder(order);
  }
  
  public Order getAllOrder() {
    return orderDao.findAll();
  }
  
  public Order getOrder(Integer orderNo) {
    long beforeTime = System.currentTimeMillis();
    Order order = orderDao.findByOrderNo(orderNo);
    long diffTime = System.currentTimeMillis() - beforeTime;
    log.info("실행시간: [{}]ms ", diffTime);
    return order;
  }
  
}
