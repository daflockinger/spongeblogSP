package com.flockinger.service;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode=ClassMode.AFTER_CLASS)
public class UserServiceTest {
	
	@Autowired
	private UserService service;
	
	@Before
	public void setup(){
		
	}
	
	@Test
	public void testContext(){
		assertNotNull(service);
	}
	
	@After
	public void teardown(){
		
	}
}
