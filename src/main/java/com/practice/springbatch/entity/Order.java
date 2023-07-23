package com.practice.springbatch.entity;

import java.util.Date;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  
  private Integer orderNo;
  private String productName;
  private String userName;
  private Integer price;
  private Date createDate;

}
