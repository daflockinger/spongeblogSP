package com.flockinger.service;

import java.util.List;

import com.flockinger.dto.UserEditDTO;
import com.flockinger.exception.DuplicateEntityException;
import com.flockinger.exception.EntityIsNotExistingException;

public interface UserService {
	UserEditDTO getUser(Long id) throws EntityIsNotExistingException;

	List<UserEditDTO> getAllUsers();

	UserEditDTO createUser(UserEditDTO user) throws DuplicateEntityException;

	void updateUser(UserEditDTO user) throws EntityIsNotExistingException;

	void deleteUser(Long id) throws EntityIsNotExistingException;
}
