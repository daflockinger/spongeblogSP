package com.flockinger.spongeblogSP.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.flockinger.spongeblogSP.dto.BlogUserDetails;
import com.flockinger.spongeblogSP.dto.UserEditDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.service.UserService;
import com.google.common.collect.ImmutableList;

public class UserControllerTest extends BaseControllerTest {


  @MockBean
  private UserService service;


  @Test
  public void testapiV1UsersGet_withFullDB_shouldReturnAll() throws Exception {
    UserEditDTO firstUser = new UserEditDTO();
    firstUser.setNickName("daflo");
    UserEditDTO secondUser = new UserEditDTO();
    secondUser.setNickName("body");
    
    when(service.getAllUsers()).thenReturn(ImmutableList.of(firstUser, secondUser));
    
    mockMvc.perform(get("/api/v1/users").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].nickName", is("daflo")))
        .andExpect(jsonPath("$[1].nickName", is("body")));
    
    verify(service).getAllUsers();
  }


  @Test
  public void testApiV1UsersUserIdGet_withValidId_shouldReturnUser() throws Exception {
    UserEditDTO firstUser = new UserEditDTO();
    firstUser.setNickName("daflo");
    when(service.getUser(anyLong())).thenReturn(firstUser);
    
    mockMvc.perform(get("/api/v1/users/1").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$.nickName", is("daflo")));
    
    verify(service).getUser(anyLong());
  }

  @Test
  public void testApiV1UsersUserIdGet_withNonExistingId_shouldReturnNotFound() throws Exception {
    when(service.getUser(anyLong())).thenThrow(EntityIsNotExistingException.class);
    
    mockMvc.perform(get("/api/v1/users/3487634").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1UsersNameUserNameGet_withValidId_shouldReturnUser() throws Exception {
    BlogUserDetails details = new BlogUserDetails();
    GrantedAuthority auth = new SimpleGrantedAuthority("ADMIN");
    details.setAuthorities(ImmutableList.of(auth));
    
    when(service.loadUserByUsername(anyString())).thenReturn(details);
    
    mockMvc.perform(get("/api/v1/users/email?address=flo@kinger.cc").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.authorities[0].authority", is("ADMIN")));
    
    verify(service).loadUserByUsername(matches("flo@kinger.cc"));
  }

  @Test
  public void testApiV1UsersNameUserNameGet_withNonExistingId_shouldReturnNotFound()
      throws Exception {
    when(service.loadUserByUsername(matches("somehacker@hackme.com"))).thenThrow(EntityIsNotExistingException.class);
    
    mockMvc
        .perform(
            get("/api/v1/users/email?address=somehacker@hackme.com").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1UsersUserIdGet_withInvalidId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/api/v1/users/totalywrong").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1UsersInfoUserIdGet_withValidId_shouldReturnUser() throws Exception {
    UserInfoDTO info = new UserInfoDTO();
    info.setNickName("daflo");
    when(service.getUserInfo(anyLong())).thenReturn(info);
    
    mockMvc.perform(get("/api/v1/users/info/1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.nickName", is("daflo")));
    
    verify(service).getUserInfo(anyLong());
  }

  @Test
  public void testApiV1UsersInfoUserIdGet_withNonExistingId_shouldReturnNotFound()
      throws Exception {
    when(service.getUserInfo(anyLong())).thenThrow(EntityIsNotExistingException.class);
    
    mockMvc.perform(get("/api/v1/users/info/3487634").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }


  @Test
  public void testApiV1UsersInfoUserIdGet_withInvalidId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/api/v1/users/info/totalywrong").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }



  @Test
  public void testApiV1UsersUserIdDelete_withValidId_shouldDelete() throws Exception {
    mockMvc.perform(delete("/api/v1/users/1").contentType(jsonContentType))
        .andExpect(status().isOk());
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(service).deleteUser(idCaptor.capture());
    assertEquals("verify user with correct ID is deleted", 1l, idCaptor.getValue().longValue());
  }

  @Test
  public void testApiV1UsersUserIdDelete_withNonExistingId_shouldReturnNotFound() throws Exception {
    doThrow(EntityIsNotExistingException.class).when(service).deleteUser(anyLong());
    
    mockMvc.perform(delete("/api/v1/users/9879797").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }


  @Test
  public void testApiV1UsersPost_withValidUser_shouldInsert() throws Exception {
    
    Date now = new Date();

    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("sepp");
    newUser.setNickName("seppi");
    newUser.setRegistered(now);
    newUser.setPassword("supersecret123");
    newUser.setEmail("sep@bla.cc");
    
    when(service.createUser(any())).thenReturn(newUser);

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
        .andExpect(status().isCreated());

    ArgumentCaptor<UserEditDTO> userCaptor = ArgumentCaptor.forClass(UserEditDTO.class);
    verify(service).createUser(userCaptor.capture());
    UserEditDTO savedUser = userCaptor.getValue();

    assertNotNull(savedUser);
    assertEquals("sepp", savedUser.getLogin());
    assertEquals("seppi", savedUser.getNickName());
    assertNotNull(savedUser.getRegistered());
    assertEquals("supersecret123", savedUser.getPassword());
    assertEquals("sep@bla.cc", savedUser.getEmail());
  }

  @Test
  public void testApiV1UsersPost_withAlreadyExistingName_shouldReturnConflict() throws Exception {
    when(service.createUser(any())).thenThrow(DuplicateEntityException.class);
    
    Date now = new Date();
    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("flo");
    newUser.setNickName("seppi");
    newUser.setRegistered(now);
    newUser.setPassword("supersecret123");
    newUser.setEmail("sep@bla.cc");

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
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

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
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

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1UsersPost_withUserWithEmptyNickName_shouldReturnBadRequest()
      throws Exception {
    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("blablu");
    newUser.setNickName("");
    newUser.setRegistered(new Date());
    newUser.setPassword("supersecret123");
    newUser.setEmail("sep@bla.cc");

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1UsersPost_withUserWithNullRegistered_shouldReturnBadRequest()
      throws Exception {
    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("blablu");
    newUser.setNickName("seppi");
    newUser.setRegistered(null);
    newUser.setPassword("supersecret123");
    newUser.setEmail("sep@bla.cc");

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1UsersPost_withUserWithEmptyPassword_shouldReturnBadRequest()
      throws Exception {
    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("blablu");
    newUser.setNickName("seppi");
    newUser.setRegistered(new Date());
    newUser.setPassword("");
    newUser.setEmail("sep@bla.cc");

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
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

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1UsersPost_withUserWithTooShortPassword_shouldReturnBadRequest()
      throws Exception {
    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("blablu");
    newUser.setNickName("seppi");
    newUser.setRegistered(new Date());
    newUser.setPassword("12345");
    newUser.setEmail("sep@bla.cc");

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1UsersPost_withUserWithNotValidDateLong_shouldReturnBadRequest()
      throws Exception {
    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("blablu");
    newUser.setNickName("seppi");
    newUser.setRegistered(new Date(-345l));
    newUser.setPassword("12345");
    newUser.setEmail("sep@bla.cc");

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1UsersPost_withUserWithNotValidEmail_shouldReturnBadRequest()
      throws Exception {
    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("blablu");
    newUser.setNickName("seppi");
    newUser.setRegistered(new Date());
    newUser.setPassword("12345");
    newUser.setEmail("www.bl.cc");

    mockMvc.perform(post("/api/v1/users").content(json(newUser)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1UsersPut_withValidUser_shouldUpdate() throws Exception {
    Date now = new Date();

    UserEditDTO existingUser = new UserEditDTO();
    existingUser.setUserId(2l);
    existingUser.setNickName("seppi");
    existingUser.setRegistered(now);
    existingUser.setPassword("supersecret123");
    existingUser.setEmail("sep@bla.cc");
    existingUser.setLogin("sepp");

    mockMvc.perform(put("/api/v1/users").content(json(existingUser)).contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<UserEditDTO> userCaptor = ArgumentCaptor.forClass(UserEditDTO.class);
    verify(service).updateUser(userCaptor.capture());
    UserEditDTO updatedUser = userCaptor.getValue();

    assertNotNull(updatedUser);
    assertEquals("seppi", updatedUser.getNickName());
    assertNotNull(updatedUser.getRegistered());
    assertEquals("supersecret123", updatedUser.getPassword());
    assertEquals("sep@bla.cc", updatedUser.getEmail());
  }

  @Test
  public void testApiV1UsersPut_withAlreadyExistingName_shouldReturnConflict() throws Exception {
    doThrow(DuplicateEntityException.class).when(service).updateUser(any());
    
    UserEditDTO user = new UserEditDTO();
    user.setUserId(2l);
    user.setEmail("flo@ckinger.com");
    user.setLogin("flo");
    user.setNickName("someone");
    user.setPassword("secrets");
    user.setRegistered(new Date());

    mockMvc.perform(put("/api/v1/users").content(json(user)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1UsersPut_withNonExistingUser_shouldReturnNotFound() throws Exception {
    doThrow(EntityIsNotExistingException.class).when(service).updateUser(any());
    
    UserEditDTO user = new UserEditDTO();
    user.setUserId(435354l);
    user.setLogin("groovy");
    user.setPassword("secrets");
    user.setRegistered(new Date());
    user.setNickName("someone");
    user.setEmail("groo@vy.cc");

    mockMvc.perform(put("/api/v1/users").content(json(user)).contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }


  @Test
  public void testApiV1UsersPut_withUserWithEmptyName_shouldReturnBadRequest() throws Exception {
    UserEditDTO user = new UserEditDTO();
    user.setUserId(1l);
    user.setLogin("");

    mockMvc.perform(post("/api/v1/users").content(json(user)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1UsersPut_withUserWithNullName_shouldReturnBadRequest() throws Exception {
    UserEditDTO user = new UserEditDTO();
    user.setUserId(1l);
    user.setLogin(null);

    mockMvc.perform(post("/api/v1/users").content(json(user)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1UsersRewindUserIdPut_withValidVersion_shouldRewind() throws Exception {

    mockMvc.perform(put("/api/v1/users/rewind/1").contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<Long> rewindIdCaptor = ArgumentCaptor.forClass(Long.class);
    verify(service).rewind(rewindIdCaptor.capture());
    assertEquals("verify user with correct it was rewound", 1l, rewindIdCaptor.getValue().longValue());
  }

  @Test
  public void testApiV1UsersRewindUserIdPut_withNoPrevVersion_shouldReturnConflict()
      throws Exception {
    doThrow(NoVersionFoundException.class).when(service).rewind(anyLong());
    
    mockMvc.perform(put("/api/v1/users/rewind/1").contentType(jsonContentType))
        .andExpect(status().isConflict());
  }
}
