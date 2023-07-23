package com.practice.springbatch.dao;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.practice.springbatch.entity.Order;

@Repository
public interface OrderDao {

  public Order createOrder(Order order);

  @Cacheable(value="Order", cacheManager = "testCacheManager")
  public Order findAll();

  @Cacheable(value="Order", key="#orderNo", cacheManager = "testCacheManager")
  public Order findByOrderNo(Integer orderNo);
  
  

}
