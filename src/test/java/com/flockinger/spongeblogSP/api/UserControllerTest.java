package com.flockinger.spongeblogSP.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.spongeblogSP.dao.UserDAO;
import com.flockinger.spongeblogSP.dto.UserEditDTO;
import com.flockinger.spongeblogSP.service.UserService;

public class UserControllerTest extends BaseControllerTest {

	
	@Autowired
	private UserService service;
	
	@Autowired
	private UserDAO dao;
	
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testapiV1UsersGet_withFullDB_shouldReturnAll() throws Exception {
		mockMvc.perform(get("/api/v1/users")
	               .contentType(jsonContentType))
					.andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(2)))
	                .andExpect(jsonPath("$[0].nickName", is("daflo")))
	                .andExpect(jsonPath("$[1].nickName", is("body")));
	}
	
	
	@Test
	@FlywayTest(invokeCleanDB=true)
	public void testapiV1UsersGet_withEmptyDB_shouldReturnEmpty() throws Exception {
		mockMvc.perform(get("/api/v1/users")
	               .contentType(jsonContentType))
					.andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(0)));
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersUserIdGet_withValidId_shouldReturnUser() throws Exception {
		mockMvc.perform(get("/api/v1/users/1")
	               .contentType(jsonContentType))
					.andExpect(status().isOk())
	                .andExpect(jsonPath("$.nickName", is("daflo")));
	}
	
	@Test
	public void testApiV1UsersUserIdGet_withNonExistingId_shouldReturnNotFound() throws Exception {
		mockMvc.perform(get("/api/v1/users/3487634")
	               .contentType(jsonContentType))
				   .andExpect(status().isNotFound());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersNameUserNameGet_withValidId_shouldReturnUser() throws Exception {
		mockMvc.perform(get("/api/v1/users/name/flo")
	               .contentType(jsonContentType))
					.andExpect(status().isOk())
	                .andExpect(jsonPath("$.authorities[0].authority", is("ADMIN")));
	}
	
	@Test
	public void testApiV1UsersNameUserNameGet_withNonExistingId_shouldReturnNotFound() throws Exception {
		mockMvc.perform(get("/api/v1/users/name/somehacker")
	               .contentType(jsonContentType))
				   .andExpect(status().isNotFound());
	}
	
	@Test
	public void testApiV1UsersUserIdGet_withInvalidId_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/users/totalywrong")
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersInfoUserIdGet_withValidId_shouldReturnUser() throws Exception {
		mockMvc.perform(get("/api/v1/users/info/1")
	               .contentType(jsonContentType))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.nickName", is("daflo")));
	}
	
	@Test
	public void testApiV1UsersInfoUserIdGet_withNonExistingId_shouldReturnNotFound() throws Exception {
		mockMvc.perform(get("/api/v1/users/info/3487634")
	               .contentType(jsonContentType))
				   .andExpect(status().isNotFound());
	}
	
	
	@Test
	public void testApiV1UsersInfoUserIdGet_withInvalidId_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/users/info/totalywrong")
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersUserIdDelete_withValidId_shouldDelete() throws Exception {
		mockMvc.perform(delete("/api/v1/users/1")
	               .contentType(jsonContentType))
				   .andExpect(status().isOk());
		
		assertFalse(dao.exists(1l));
	}
	
	@Test
	public void testApiV1UsersUserIdDelete_withNonExistingId_shouldReturnNotFound() throws Exception {
		mockMvc.perform(delete("/api/v1/users/9879797")
	               .contentType(jsonContentType))
				   .andExpect(status().isNotFound());
	}
	

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersPost_withValidUser_shouldInsert() throws Exception {
		Date now = new Date();

		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("sepp");
		newUser.setNickName("seppi");
		newUser.setRegistered(now);
		newUser.setPassword("supersecret123");
		newUser.setEmail("sep@bla.cc");

		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isCreated());

		UserEditDTO savedUser = service.getUser(3l);

		assertNotNull(savedUser);
		assertEquals("sepp", savedUser.getLogin());
		assertEquals("seppi", savedUser.getNickName());
		assertNotNull(savedUser.getRegistered());
		assertEquals("supersecret123", savedUser.getPassword());
		assertEquals("sep@bla.cc", savedUser.getEmail());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersPost_withAlreadyExistingName_shouldReturnConflict() throws Exception {
		Date now = new Date();

		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("flo");
		newUser.setNickName("seppi");
		newUser.setRegistered(now);
		newUser.setPassword("supersecret123");
		newUser.setEmail("sep@bla.cc");
		
		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isConflict());
	}
	
	@Test
	public void testApiV1UsersPost_withUserWithEmptyLogin_shouldReturnBadRequest() throws Exception {
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("");
		newUser.setNickName("seppi");
		newUser.setRegistered(new Date());
		newUser.setPassword("supersecret123");
		newUser.setEmail("sep@bla.cc");
		
		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void testApiV1UsersPost_withUserWithNullLogin_shouldReturnBadRequest() throws Exception {
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin(null);
		newUser.setNickName("seppi");
		newUser.setRegistered(new Date());
		newUser.setPassword("supersecret123");
		newUser.setEmail("sep@bla.cc");
		
		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1UsersPost_withUserWithEmptyNickName_shouldReturnBadRequest() throws Exception {
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("blablu");
		newUser.setNickName("");
		newUser.setRegistered(new Date());
		newUser.setPassword("supersecret123");
		newUser.setEmail("sep@bla.cc");
		
		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void testApiV1UsersPost_withUserWithNullRegistered_shouldReturnBadRequest() throws Exception {
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("blablu");
		newUser.setNickName("seppi");
		newUser.setRegistered(null);
		newUser.setPassword("supersecret123");
		newUser.setEmail("sep@bla.cc");
		
		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1UsersPost_withUserWithEmptyPassword_shouldReturnBadRequest() throws Exception {
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("blablu");
		newUser.setNickName("seppi");
		newUser.setRegistered(new Date());
		newUser.setPassword("");
		newUser.setEmail("sep@bla.cc");
		
		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1UsersPost_withUserWithEmptyEmail_shouldReturnBadRequest() throws Exception {
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("blablu");
		newUser.setNickName("seppi");
		newUser.setRegistered(new Date());
		newUser.setPassword("supersecret123");
		newUser.setEmail("");
		
		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1UsersPost_withUserWithTooShortPassword_shouldReturnBadRequest() throws Exception {
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("blablu");
		newUser.setNickName("seppi");
		newUser.setRegistered(new Date());
		newUser.setPassword("12345");
		newUser.setEmail("sep@bla.cc");
		
		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1UsersPost_withUserWithNotValidDateLong_shouldReturnBadRequest() throws Exception {
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("blablu");
		newUser.setNickName("seppi");
		newUser.setRegistered(new Date(-345l));
		newUser.setPassword("12345");
		newUser.setEmail("sep@bla.cc");
		
		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1UsersPost_withUserWithNotValidEmail_shouldReturnBadRequest() throws Exception {
		UserEditDTO newUser = new UserEditDTO();
		newUser.setLogin("blablu");
		newUser.setNickName("seppi");
		newUser.setRegistered(new Date());
		newUser.setPassword("12345");
		newUser.setEmail("www.bl.cc");
		
		mockMvc.perform(post("/api/v1/users").content(json(newUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
		
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersPut_withValidUser_shouldUpdate() throws Exception {
		Date now = new Date();

		UserEditDTO existingUser = service.getUser(1l);
		existingUser.setNickName("seppi");
		existingUser.setRegistered(now);
		existingUser.setPassword("supersecret123");
		existingUser.setEmail("sep@bla.cc");

		mockMvc.perform(put("/api/v1/users").content(json(existingUser))
	               .contentType(jsonContentType))
				   .andExpect(status().isOk());

		UserEditDTO updatedUser = service.getUser(1l);

		assertNotNull(updatedUser);
		assertEquals("seppi", updatedUser.getNickName());
		assertNotNull(updatedUser.getRegistered());
		assertEquals("supersecret123", updatedUser.getPassword());
		assertEquals("sep@bla.cc", updatedUser.getEmail());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersPut_withAlreadyExistingName_shouldReturnConflict() throws Exception {
		UserEditDTO user = service.getUser(2l);
		user.setLogin("flo");
		
		mockMvc.perform(put("/api/v1/users").content(json(user))
	               .contentType(jsonContentType))
				   .andExpect(status().isConflict());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersPut_withNonExistingUser_shouldReturnNotFound() throws Exception {
		UserEditDTO user = service.getUser(1l);
		user.setUserId(435354l);
		user.setLogin("groovy");
		
		mockMvc.perform(put("/api/v1/users").content(json(user))
	               .contentType(jsonContentType))
				   .andExpect(status().isNotFound());
	}
	
	
	@Test
	public void  testApiV1UsersPut_withUserWithEmptyName_shouldReturnBadRequest() throws Exception {
		UserEditDTO user = new UserEditDTO();
		user.setUserId(1l);
		user.setLogin("");
		
		mockMvc.perform(post("/api/v1/users").content(json(user))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void  testApiV1UsersPut_withUserWithNullName_shouldReturnBadRequest() throws Exception {
		UserEditDTO user = new UserEditDTO();
		user.setUserId(1l);
		user.setLogin(null);
		
		mockMvc.perform(post("/api/v1/users").content(json(user))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersRewindUserIdPut_withValidVersion_shouldRewind() throws Exception {
	

		UserEditDTO freshUser = service.getUser(1l);
		freshUser.setRegistered(new Date());
		service.updateUser(freshUser);
		
		UserEditDTO existingUser = service.getUser(1l);
		existingUser.setNickName("seppi");
		existingUser.setRegistered(new Date());
		existingUser.setPassword("supersecret123");
		existingUser.setEmail("sep@bla.cc");

		service.updateUser(existingUser);

		UserEditDTO updatedUser = service.getUser(1l);
		assertNotNull(updatedUser);
		assertEquals("seppi", updatedUser.getNickName());
		assertNotNull(updatedUser.getRegistered());
		assertEquals("supersecret123", updatedUser.getPassword());
		assertEquals("sep@bla.cc", updatedUser.getEmail());
		
		mockMvc.perform(put("/api/v1/users/rewind/1")
	               .contentType(jsonContentType))
				   .andExpect(status().isOk());
		
		UserEditDTO rewindUser = service.getUser(1l);
		assertNotNull(rewindUser);
		assertEquals("daflo", rewindUser.getNickName());
		assertNotNull(rewindUser.getRegistered());
		assertEquals("secret", rewindUser.getPassword());
		assertEquals("flo@kinger.cc", rewindUser.getEmail());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1UsersRewindUserIdPut_withNoPrevVersion_shouldReturnConflict() throws Exception {
		mockMvc.perform(put("/api/v1/users/rewind/1")
	               .contentType(jsonContentType))
				   .andExpect(status().isConflict());
	}
}
