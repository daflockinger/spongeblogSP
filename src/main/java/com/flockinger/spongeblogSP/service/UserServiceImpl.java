package com.flockinger.spongeblogSP.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dao.UserDAO;
import com.flockinger.spongeblogSP.dto.UserEditDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.model.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO dao;
	
	@Autowired
	private PostDAO postDao;

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

		return map(dao.save(map(user)));
	}

	private boolean isUserExistingAlready(UserEditDTO user) {
		return isNotEmpty(user.getLogin()) && dao.findByLogin(user.getLogin()) != null;
	}

	@Override
	@Transactional
	public void updateUser(UserEditDTO user) throws EntityIsNotExistingException {
		if (!dao.exists(user.getId())) {
			throw new EntityIsNotExistingException("User");
		} 
		
		dao.save(map(user));
	}

	@Override
	@Transactional
	public void deleteUser(Long id) throws EntityIsNotExistingException {
		if (!dao.exists(id)) {
			throw new EntityIsNotExistingException("User with ID " + id);
		}
		List<Post> posts = postDao.findByAuthorId(id);
		posts.forEach(post -> post.setAuthor(null));
		
		postDao.save(posts);
		
		dao.delete(id);
	}

	private User map(UserEditDTO userDTO) {
		return mapper.map(userDTO, User.class);
	}

	private UserEditDTO map(User user) {
		return mapper.map(user, UserEditDTO.class);
	}
}
