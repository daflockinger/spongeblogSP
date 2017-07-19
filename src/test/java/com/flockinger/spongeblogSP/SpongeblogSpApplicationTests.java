package com.flockinger.spongeblogSP;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.spongeblogSP.service.BaseServiceTest;
import com.flockinger.spongeblogSP.service.UserService;

public class SpongeblogSpApplicationTests extends BaseServiceTest {

  @Autowired
  private UserService service;

  @Test
  public void contextLoads() {
    assertNotNull(service);
  }
}
