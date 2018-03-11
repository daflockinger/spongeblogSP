package com.flockinger.spongeblogSP.api.impl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.UserController;
import com.flockinger.spongeblogSP.api.util.RequestValidator;
import com.flockinger.spongeblogSP.dto.UserEditDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.service.UserService;

import io.swagger.annotations.ApiParam;

@RestController
public class UserControllerImpl implements UserController {

  @Autowired
  private UserService service;
  @Autowired
  private RequestValidator validator;

  public ResponseEntity<List<UserEditDTO>> apiV1UsersGet() {

    List<UserEditDTO> users = service.getAllUsers();
    return new ResponseEntity<List<UserEditDTO>>(users, HttpStatus.OK);
  }

  public ResponseEntity<UserInfoDTO> apiV1UsersInfoUserIdGet(
      @ApiParam(value = "Unique identifier of a User;",
          required = true) @PathVariable("userId") Long userId)
      throws EntityIsNotExistingException {

    UserInfoDTO userInfo = service.getUserInfo(userId);
    return new ResponseEntity<UserInfoDTO>(userInfo, HttpStatus.OK);
  }

  public ResponseEntity<UserEditDTO> apiV1UsersPost(
      @ApiParam(value = "", required = true) @Valid @RequestBody UserEditDTO userEdit,
      BindingResult bindingResult) throws DuplicateEntityException, DtoValidationFailedException {

    validator.validateRequestBody(bindingResult);
    UserEditDTO user = service.createUser(userEdit);
    return new ResponseEntity<UserEditDTO>(user, HttpStatus.CREATED);
  }

  public ResponseEntity<Void> apiV1UsersPut(
      @ApiParam(value = "", required = true) @Valid @RequestBody UserEditDTO userEdit,
      BindingResult bindingResult)
      throws EntityIsNotExistingException, DtoValidationFailedException, DuplicateEntityException {

    validator.validateRequestBody(bindingResult);
    validator.assertIdForUpdate(userEdit.getUserId());
    service.updateUser(userEdit);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  public ResponseEntity<Void> apiV1UsersRewindUserIdPut(
      @ApiParam(value = "Unique identifier of a User;",
          required = true) @PathVariable("userId") Long userId)
      throws NoVersionFoundException {

    service.rewind(userId);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  public ResponseEntity<Void> apiV1UsersUserIdDelete(
      @ApiParam(value = "Unique identifier of a User;",
          required = true) @PathVariable("userId") Long userId)
      throws EntityIsNotExistingException {

    service.deleteUser(userId);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  public ResponseEntity<UserEditDTO> apiV1UsersUserIdGet(
      @ApiParam(value = "Unique identifier of a User;",
          required = true) @PathVariable("userId") Long userId)
      throws EntityIsNotExistingException {

    UserEditDTO user = service.getUser(userId);
    return new ResponseEntity<UserEditDTO>(user, HttpStatus.OK);
  }

  public ResponseEntity<UserDetails> apiV1UsersNameUserNameGet(
      @ApiParam(value = "Email of the user.", required = true) @RequestParam(value = "address",
          required = true) String email) {
    return new ResponseEntity<UserDetails>(service.loadUserByUsername(email), HttpStatus.OK);
  }
}
