package com.flockinger.spongeblogSP.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.flockinger.spongeblogSP.dto.UserEditDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;

public interface UserService extends Versionable, UserDetailsService {
  UserEditDTO getUser(Long id) throws EntityIsNotExistingException;

  UserInfoDTO getUserInfo(Long id) throws EntityIsNotExistingException;

  List<UserEditDTO> getAllUsers();

  UserEditDTO createUser(UserEditDTO user) throws DuplicateEntityException;

  void updateUser(UserEditDTO user) throws EntityIsNotExistingException, DuplicateEntityException;

  void deleteUser(Long id) throws EntityIsNotExistingException;
}
