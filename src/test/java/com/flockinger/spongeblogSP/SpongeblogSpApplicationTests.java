package com.flockinger.spongeblogSP;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.flockinger.spongeblogSP.dao.UserDAO;
import com.flockinger.spongeblogSP.service.BaseServiceTest;
import com.flockinger.spongeblogSP.service.UserService;
import com.flockinger.spongeblogSP.service.impl.UserServiceImpl;

public class SpongeblogSpApplicationTests extends BaseServiceTest{
	
	@Autowired
	private UserService service;

	@Test
	public void contextLoads() {
		assertNotNull(service);
	}
}
