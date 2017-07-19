package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.flockinger.spongeblogSP.dto.LoginDTO;
import com.flockinger.spongeblogSP.dto.UserEditDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;

public class UserServiceTest extends BaseServiceTest {

  @Autowired
  private UserService service;

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetUser_withValidId_shouldReturnUser() throws EntityIsNotExistingException {
    UserEditDTO user = service.getUser(1l);

    assertNotNull(user);
    assertEquals("flo", user.getLogin());
    assertEquals("daflo", user.getNickName());
    assertNotNull(user.getRegistered());
    assertEquals("flo@kinger.cc", user.getEmail());
    assertEquals("secret", user.getPassword());
  }

  @Test(expected = EntityIsNotExistingException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetUser_withNotExistingId_shouldThrowNotFoundException()
      throws EntityIsNotExistingException {
    service.getUser(12345l);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetUserInfo_withValidId_shouldReturnUser() throws EntityIsNotExistingException {
    UserInfoDTO user = service.getUserInfo(1l);

    assertNotNull(user);
    assertEquals("daflo", user.getNickName());
    assertNotNull(user.getRegistered());
    assertEquals("flo@kinger.cc", user.getEmail());
  }

  @Test(expected = EntityIsNotExistingException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetUserInfo_withNotExistingId_shouldThrowNotFoundException()
      throws EntityIsNotExistingException {
    service.getUserInfo(12345l);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllUsers_shouldReturnOne() throws EntityIsNotExistingException {
    List<UserEditDTO> users = service.getAllUsers();

    assertNotNull(users);
    assertTrue(users.size() == 2);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateUser_withValidData_shouldWork()
      throws EntityIsNotExistingException, DuplicateEntityException {
    Date now = new Date();

    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("sepp");
    newUser.setNickName("seppi");
    newUser.setRegistered(now);
    newUser.setPassword("supersecret123");
    newUser.setEmail("sep@bla.cc");

    Long savedId = service.createUser(newUser).getUserId();

    UserEditDTO savedUser = service.getUser(savedId);

    assertNotNull(savedUser);
    assertEquals("sepp", savedUser.getLogin());
    assertEquals("seppi", savedUser.getNickName());
    assertNotNull(savedUser.getRegistered());
    assertEquals("supersecret123", savedUser.getPassword());
    assertEquals("sep@bla.cc", savedUser.getEmail());

  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdateUser_withValidData_shouldWork()
      throws EntityIsNotExistingException, DuplicateEntityException {
    Date now = new Date();

    UserEditDTO existingUser = service.getUser(1l);
    existingUser.setNickName("seppi");
    existingUser.setRegistered(now);
    existingUser.setPassword("supersecret123");
    existingUser.setEmail("sep@bla.cc");

    service.updateUser(existingUser);

    UserEditDTO updatedUser = service.getUser(1l);

    assertNotNull(updatedUser);
    assertEquals("seppi", updatedUser.getNickName());
    assertNotNull(updatedUser.getRegistered());
    assertEquals("supersecret123", updatedUser.getPassword());
    assertEquals("sep@bla.cc", updatedUser.getEmail());

  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdateUser_withEmptyPassword_shouldNotOverwriteButKeepOldOne()
      throws EntityIsNotExistingException, DuplicateEntityException {
    Date now = new Date();

    UserEditDTO existingUser = service.getUser(1l);
    existingUser.setNickName("seppi");
    existingUser.setRegistered(now);
    existingUser.setPassword("");
    existingUser.setEmail("sep@bla.cc");

    service.updateUser(existingUser);

    UserEditDTO updatedUser = service.getUser(1l);

    assertNotNull(updatedUser);
    assertEquals("seppi", updatedUser.getNickName());
    assertNotNull(updatedUser.getRegistered());
    assertEquals("secret", updatedUser.getPassword());
    assertEquals("sep@bla.cc", updatedUser.getEmail());

  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testDeleteUser_withValidData_shouldWork()
      throws EntityIsNotExistingException, DuplicateEntityException {
    service.deleteUser(1l);

    assertTrue(service.getAllUsers().size() == 1);
  }

  @Test(expected = DuplicateEntityException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateUser_withAlreadyExistingUsername_shouldThrowDuplicateException()
      throws EntityIsNotExistingException, DuplicateEntityException {
    Date now = new Date();

    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("flo");
    newUser.setNickName("seppi");
    newUser.setRegistered(now);
    newUser.setPassword("supersecret123");

    service.createUser(newUser);
  }

  @Test(expected = ConstraintViolationException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateUser_withNoCreationDate_shouldThrowException()
      throws EntityIsNotExistingException, DuplicateEntityException {
    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin("kkkkflo");
    newUser.setNickName("seppi");
    newUser.setPassword("supersecret");

    service.createUser(newUser);
  }

  @Test(expected = ConstraintViolationException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateUser_withNoLogin_shouldThrowException()
      throws EntityIsNotExistingException, DuplicateEntityException {
    Date now = new Date();

    UserEditDTO newUser = new UserEditDTO();
    newUser.setNickName("seppi");
    newUser.setRegistered(now);
    newUser.setPassword("supersecret");

    service.createUser(newUser);
  }

  @Test(expected = ConstraintViolationException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateUser_withTooLongLogin_shouldThrowException()
      throws EntityIsNotExistingException, DuplicateEntityException {
    Date now = new Date();

    UserEditDTO newUser = new UserEditDTO();
    newUser.setLogin(generateTooLongLoginName());
    newUser.setNickName("seppi");
    newUser.setRegistered(now);
    newUser.setPassword("supersecret");

    service.createUser(newUser);
  }

  private String generateTooLongLoginName() {
    StringBuilder loginName = new StringBuilder();

    for (int i = 0; i < 151; i++) {
      loginName.append("b");
    }
    return loginName.toString();
  }

  @Test(expected = DuplicateEntityException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdateUser_withOtherExistingLoginName_shouldThrowIntegrityException()
      throws EntityIsNotExistingException, DuplicateEntityException {
    UserEditDTO existingUser = service.getUser(1l);
    existingUser.setLogin("nobody");

    service.updateUser(existingUser);
  }

  @Test(expected = EntityIsNotExistingException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testDeleteUser_withNotExistingId_shouldThrowNotFoundException()
      throws EntityIsNotExistingException {
    service.deleteUser(12345l);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testIsLoginCorrect_withValidCredentials_shouldReturnTrue() {
    LoginDTO login = new LoginDTO();
    login.setLogin("flo");
    login.setPassword("secret");
    assertTrue(service.isLoginCorrect(login));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testIsLoginCorrect_withWrongUserAndPass_shouldReturnFalse() {
    LoginDTO login = new LoginDTO();
    login.setLogin("hacker");
    login.setPassword("1234");
    assertFalse(service.isLoginCorrect(login));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testIsLoginCorrect_withWrongUser_shouldReturnFalse() {
    LoginDTO login = new LoginDTO();
    login.setLogin("hacker");
    login.setPassword("secret");
    assertFalse(service.isLoginCorrect(login));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testIsLoginCorrect_withWrongPass_shouldReturnFalse() {
    LoginDTO login = new LoginDTO();
    login.setLogin("flo");
    login.setPassword("1234");
    assertFalse(service.isLoginCorrect(login));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testRewind_withExistingPrevVersion_shouldRewind()
      throws com.flockinger.spongeblogSP.exception.NoVersionFoundException,
      DuplicateEntityException, EntityIsNotExistingException {
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

    service.rewind(1l);

    UserEditDTO rewindUser = service.getUser(1l);
    assertNotNull(rewindUser);
    assertEquals("daflo", rewindUser.getNickName());
    assertNotNull(rewindUser.getRegistered());
    assertEquals("secret", rewindUser.getPassword());
    assertEquals("flo@kinger.cc", rewindUser.getEmail());

  }

  @Test(expected = NoVersionFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testRewind_withNoPreviousVersion_shouldThrowException()
      throws NoVersionFoundException, DuplicateEntityException, EntityIsNotExistingException {
    service.rewind(2l);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testLoadUserByUsername_withExistingUsername_shouldReturnCorrect() {
    UserDetails details = service.loadUserByUsername("flo@kinger.cc");

    assertNotNull(details);
    assertEquals("secret", details.getPassword());
    assertEquals("flo@kinger.cc", details.getUsername());
    assertTrue(details.isAccountNonExpired());
    assertTrue(details.isAccountNonLocked());
    assertTrue(details.isCredentialsNonExpired());
    assertTrue(details.isEnabled());
    assertNotNull(details.getAuthorities());
    assertTrue(details.getAuthorities().size() == 1);
    assertTrue(
        details.getAuthorities().stream().allMatch(auth -> auth.getAuthority().equals("ADMIN")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testLoadUserByUsername_withExistingUsernameButNoRoles_shouldReturnCorrect() {
    UserDetails details = service.loadUserByUsername("no@body.cc");
    assertNotNull(details);
  }

  @Test(expected = UsernameNotFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testLoadUserByUsername_withNonExistingUsername_shouldThrowException()
      throws UsernameNotFoundException {
    service.loadUserByUsername("nonExist");
  }
}
