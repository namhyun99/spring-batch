package com.practice.springbatch.entity.type.group;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.practice.springbatch.entity.type.PayType;

public enum PayGroup {
  CASH("현금", Arrays.asList(PayType.ACCOUNT_TRANSFER, PayType.REMITTANCE, PayType.ON_SITE_PAYMENT, PayType.TOSS)),
  EMPTY("없음",Collections.EMPTY_LIST);
  
  private String title;
  private List<PayType> payList;
  
  PayGroup(String title, List<PayType> payList){
    this.title = title;
    this.payList = payList;
  }
  
  public static PayGroup findByPayType(PayType payType) {
    return Arrays.stream(PayGroup.values())
        .filter(payGroup -> payGroup.hasPayCode(payType))
        .findAny()
        .orElse(EMPTY);
  }

  public boolean hasPayCode(PayType payType) {
    return payList.stream().anyMatch(pay -> pay == payType);
  }
  
  public String getTitle() {
    return title;
  }
  
  

}
