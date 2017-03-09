package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.dto.UserEditDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_CLASS)
public class UserServiceTest {
	
	@Autowired
	private UserService service;
	
	
	@Test
	public void testGetUser_withValidId_shouldReturnUser() throws EntityIsNotExistingException{
		UserEditDTO user = service.getUser(1l);
		
		assertNotNull(user);
		assertEquals("flo", user.getLogin());
		assertEquals("daflo", user.getNickName());
		assertNotNull(user.getRegistered());
		assertEquals("flo@kinger.cc", user.getEmail());
		assertEquals("secret", user.getPassword());
	}
	
	@Test(expected=EntityIsNotExistingException.class)
	public void testGetUser_withNotExistingId_shouldThrowNotFoundException() throws EntityIsNotExistingException{
		service.getUser(12345l);
	}
	
	@Test
	public void testGetAllUsers_shouldReturnOne() throws EntityIsNotExistingException{
		List<UserEditDTO> users = service.getAllUsers();
		
		assertNotNull(users);
		assertTrue(users.size() == 2);
	}
	
	@Test
	public void testCreateUpdateAndDeleteUser_withValidData_shouldWork() throws EntityIsNotExistingException, DuplicateEntityException{
		Date now = new Date();
		
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("sepp");
		newUser.setNickName("seppi");
		newUser.setRegistered(now);
		newUser.setPassword("supersecret123");
		newUser.setEmail("sep@bla.cc");
		
		Long savedId = service.createUser(newUser).getId();
		
		UserEditDTO savedUser = service.getUser(savedId);
		
		assertNotNull(savedUser);
		assertEquals("sepp", savedUser.getLogin());
		assertEquals("seppi", savedUser.getNickName());
		assertNotNull(savedUser.getRegistered());
		assertEquals("supersecret123", savedUser.getPassword());
		assertEquals("sep@bla.cc", savedUser.getEmail());
		
		
		UserEditDTO existingUser = service.getUser(savedId);
		existingUser.setNickName("seppi");
		existingUser.setRegistered(now);
		existingUser.setPassword("supersecret123");
		existingUser.setEmail("sep@bla.cc");
		
		service.updateUser(existingUser);
		
		UserEditDTO updatedUser = service.getUser(savedId);
		
		assertNotNull(updatedUser);
		assertEquals("seppi", updatedUser.getNickName());
		assertNotNull(updatedUser.getRegistered());
		assertEquals("supersecret123", updatedUser.getPassword());
		assertEquals("sep@bla.cc", updatedUser.getEmail());
		
		service.deleteUser(savedId);
		
		assertTrue(service.getAllUsers().size() == 2);
	}
	
	@Test(expected=DuplicateEntityException.class)
	public void testCreateUser_withAlreadyExistingUsername_shouldThrowDuplicateException() throws EntityIsNotExistingException, DuplicateEntityException{
		Date now = new Date();
		
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("flo");
		newUser.setNickName("seppi");
		newUser.setRegistered(now);
		newUser.setPassword("supersecret123");
		
		service.createUser(newUser);
	}
	
	
	@Test(expected=ConstraintViolationException.class)
	public void testCreateUser_withNoPassword_shouldThrowException() throws EntityIsNotExistingException, DuplicateEntityException{
		Date now = new Date();
		
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("kkkkflo");
		newUser.setNickName("seppi");
		newUser.setRegistered(now);
		
		service.createUser(newUser);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void testCreateUser_withNoCreationDate_shouldThrowException() throws EntityIsNotExistingException, DuplicateEntityException{		
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("kkkkflo");
		newUser.setNickName("seppi");
		newUser.setPassword("supersecret");
		
		service.createUser(newUser);
	}
	
	
	@Test(expected=ConstraintViolationException.class)
	public void testCreateUser_withNoLogin_shouldThrowException() throws EntityIsNotExistingException, DuplicateEntityException{
		Date now = new Date();
		
		UserEditDTO newUser = new UserEditDTO();
		newUser.setNickName("seppi");
		newUser.setRegistered(now);
		newUser.setPassword("supersecret");
		
		service.createUser(newUser);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void testCreateUser_withTooLongLogin_shouldThrowException() throws EntityIsNotExistingException, DuplicateEntityException{
		Date now = new Date();
		
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin(generateTooLongLoginName());
		newUser.setNickName("seppi");
		newUser.setRegistered(now);
		newUser.setPassword("supersecret");
		
		service.createUser(newUser);
	}
	
	private String generateTooLongLoginName(){
		return RandomStringUtils.random(151);
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void testUpdateUser_withOtherExistingLoginName_shouldThrowIntegrityException() throws EntityIsNotExistingException, DuplicateEntityException{		
		UserEditDTO existingUser = service.getUser(1l);
		existingUser.setLogin("nobody");
		
		service.updateUser(existingUser);
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void testDeleteUser_withExistingIdButBoundToPosts_shouldThrowIntegrityException() throws EntityIsNotExistingException{
		service.deleteUser(1l);
	}
	
	@Test(expected=EntityIsNotExistingException.class)
	public void testDeleteUser_withNotExistingId_shouldThrowNotFoundException() throws EntityIsNotExistingException{
		service.deleteUser(12345l);
	}
}
