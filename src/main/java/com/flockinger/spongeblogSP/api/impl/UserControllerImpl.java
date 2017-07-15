package com.flockinger.spongeblogSP.api.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.UserController;
import com.flockinger.spongeblogSP.api.util.RequestValidator;
import com.flockinger.spongeblogSP.dto.BlogUserDetails;
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

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<List<UserEditDTO>> apiV1UsersGet() {

		List<UserEditDTO> users = service.getAllUsers();
		users.forEach(user -> addSelfLink(user));
		return new ResponseEntity<List<UserEditDTO>>(users, HttpStatus.OK);
	}

	public ResponseEntity<UserInfoDTO> apiV1UsersInfoUserIdGet(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId)
			throws EntityIsNotExistingException {

		UserInfoDTO userInfo = service.getUserInfo(userId);
		return new ResponseEntity<UserInfoDTO>(addSelfLink(userInfo), HttpStatus.OK);
	}

	public ResponseEntity<UserEditDTO> apiV1UsersPost(
			@ApiParam(value = "", required = true) @Valid @RequestBody UserEditDTO userEdit,
			BindingResult bindingResult) throws DuplicateEntityException, DtoValidationFailedException {

		validator.validateRequestBody(bindingResult);
		UserEditDTO user = service.createUser(userEdit);
		return new ResponseEntity<UserEditDTO>(addSelfLink(user), HttpStatus.CREATED);
	}

	public ResponseEntity<Void> apiV1UsersPut(
			@ApiParam(value = "", required = true) @Valid @RequestBody UserEditDTO userEdit,
			BindingResult bindingResult) throws EntityIsNotExistingException, DtoValidationFailedException, DuplicateEntityException {

		validator.validateRequestBody(bindingResult);
		validator.assertIdForUpdate(userEdit.getUserId());
		service.updateUser(userEdit);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<Void> apiV1UsersRewindUserIdPut(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId) throws NoVersionFoundException {
		
		service.rewind(userId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<Void> apiV1UsersUserIdDelete(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId)
			throws EntityIsNotExistingException {

		service.deleteUser(userId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<UserEditDTO> apiV1UsersUserIdGet(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId)
			throws EntityIsNotExistingException {

		UserEditDTO user = service.getUser(userId);
		return new ResponseEntity<UserEditDTO>(addSelfLink(user), HttpStatus.OK);
	}

    public ResponseEntity<UserDetails> apiV1UsersNameUserNameGet(@ApiParam(value = "Login name of the user.",required=true ) @PathVariable("userName") String userName) {
        return new ResponseEntity<UserDetails>(service.loadUserByUsername(userName),HttpStatus.OK);
    }
	
	private UserEditDTO addSelfLink(UserEditDTO user) {
		try {
			user.add(linkTo(methodOn(UserControllerImpl.class).apiV1UsersUserIdGet(user.getUserId())).withSelfRel());
		} catch (EntityIsNotExistingException e) {
			logger.error("Not found after Persisting. Should not happen.");
		}
		return user;
	}

	private UserInfoDTO addSelfLink(UserInfoDTO user) {
		try {
			user.add(
					linkTo(methodOn(UserControllerImpl.class).apiV1UsersInfoUserIdGet(user.getUserId())).withSelfRel());
		} catch (EntityIsNotExistingException e) {
			logger.error("Not found after Persisting. Should not happen.");
		}
		return user;
	}
}
