package com.practice.springbatch;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.practice.springbatch.entity.type.PayType;
import com.practice.springbatch.entity.type.group.PayGroup;

class SpringBatchApplicationTests {
  
  @Test
  public void PayGroupTest() throws Exception{
    PayType payType = PayType.ACCOUNT_TRANSFER;
    PayGroup payGroup = PayGroup.findByPayType(payType);
    
    assertThat(payGroup.name()).isEqualTo("CASH");
    assertThat(payGroup.getTitle()).isEqualTo("현금");
  }
  
}
