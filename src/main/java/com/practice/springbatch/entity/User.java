package com.practice.springbatch.entity;

import java.util.Date;

import com.practice.springbatch.entity.type.UserState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private String id;
  private String name;
  private UserState state;
  private Date lastActionDate;
}
