package com.practice.springbatch.entity;

import java.util.Date;

import com.practice.springbatch.entity.type.UserState;

import lombok.Data;

@Data
public class User {
  private String id;
  private String name;
  private UserState state;
  private Date lastActionDate;
}
