package com.flockinger.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.dao.UserDAO;
import com.flockinger.dto.UserEditDTO;
import com.flockinger.exception.DuplicateEntityException;
import com.flockinger.exception.EntityIsNotExistingException;
import com.flockinger.model.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO dao;

	@Autowired
	private ModelMapper mapper;

	@Override
	@Transactional(readOnly = true)
	public UserEditDTO getUser(Long id) throws EntityIsNotExistingException {
		if (dao.exists(id)) {
			return map(dao.findOne(id));
		} else {
			throw new EntityIsNotExistingException("User with ID " + id);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEditDTO> getAllUsers() {
		List<UserEditDTO> userDTOs = new ArrayList<>();
		Iterable<User> users = dao.findAll();

		users.iterator().forEachRemaining(user -> userDTOs.add(map(user)));

		return userDTOs;
	}

	@Override
	@Transactional
	public UserEditDTO createUser(UserEditDTO user) throws DuplicateEntityException {
		if (isUserExistingAlready(user)) {
			throw new DuplicateEntityException("User " + user.getLogin());
		}

		dao.save(map(user));

		return user;
	}

	private boolean isUserExistingAlready(UserEditDTO user) {
		return isNotEmpty(user.getLogin()) && dao.findByLogin(user.getLogin()) != null;
	}

	@Override
	@Transactional
	public void updateUser(UserEditDTO user) throws EntityIsNotExistingException {
		if ((user.getId() != null) && dao.exists(user.getId())) {
			dao.save(map(user));
		} else {
			throw new EntityIsNotExistingException("User");
		}
	}

	@Override
	@Transactional
	public void deleteUser(Long id) throws EntityIsNotExistingException {
		if (dao.exists(id)) {
			dao.delete(id);
		} else {
			throw new EntityIsNotExistingException("User with ID " + id);
		}
	}

	public void setMapper(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public void setDao(UserDAO dao) {
		this.dao = dao;
	}

	private User map(UserEditDTO userDTO) {
		return mapper.map(userDTO, User.class);
	}

	private UserEditDTO map(User user) {
		return mapper.map(user, UserEditDTO.class);
	}
}
