package com.flockinger;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.flockinger.dao.UserDAO;
import com.flockinger.service.UserService;
import com.flockinger.service.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpongeblogSpApplicationTests {
	
	@Autowired
	private UserService service;

	@Test
	public void contextLoads() {
		assertNotNull(service);
	}
}
