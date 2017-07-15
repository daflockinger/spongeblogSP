package com.flockinger.spongeblogSP.service.impl;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dao.UserDAO;
import com.flockinger.spongeblogSP.dto.BlogAuthority;
import com.flockinger.spongeblogSP.dto.BlogUserDetails;
import com.flockinger.spongeblogSP.dto.LoginDTO;
import com.flockinger.spongeblogSP.dto.UserEditDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.model.User;
import com.flockinger.spongeblogSP.service.UserService;
import com.flockinger.spongeblogSP.service.VersioningService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO dao;
	
	@Autowired
	private PostDAO postDao;

	private ModelMapper mapper;
	
	@Autowired
	private VersioningService<User,UserDAO> versionService;

	@Autowired
	public UserServiceImpl(ModelMapper mapper){
		this.mapper = mapper;
		
		mapper.addMappings(new PropertyMap<User, BlogUserDetails>() {
			@Override
			protected void configure() {
				map().setUsername(source.getLogin());
			}
		});
	}
	
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
	public UserInfoDTO getUserInfo(Long id) throws EntityIsNotExistingException {
		if (dao.exists(id)) {
			return mapInfo(dao.findOne(id));
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
	@CacheEvict(value="users",allEntries=true)
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
	@CacheEvict(value="users",allEntries=true)
	public void updateUser(UserEditDTO user) throws EntityIsNotExistingException, DuplicateEntityException {
		if (!dao.exists(user.getUserId())) {
			throw new EntityIsNotExistingException("User");
		} 
		if(isLoginDuplicate(user)){
			throw new DuplicateEntityException("User " + user.getLogin());
		}
		
		dao.save(map(user));
	}
	
	private boolean isLoginDuplicate(UserEditDTO updatedUser) {
		User user = dao.findByLogin(updatedUser.getLogin());
		return user != null && user.getId() != updatedUser.getUserId();
	}

	@Override
	@Transactional
	@CacheEvict(value="users",allEntries=true)
	public void deleteUser(Long id) throws EntityIsNotExistingException {
		if (!dao.exists(id)) {
			throw new EntityIsNotExistingException("User with ID " + id);
		}
		List<Post> posts = postDao.findByAuthorId(id);
		posts.forEach(post -> post.setAuthor(null));
		
		postDao.save(posts);
		
		dao.delete(id);
	}
	
	@Override
	@Transactional
	@Cacheable(value="users")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = dao.findByLogin(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		BlogUserDetails userDetails = mapUserDetails(user);
		userDetails.setEnabled(user.getRegistered() != null);
		
		List<BlogAuthority> authorities = user.getRoles().stream()
				.map(role -> new BlogAuthority(role.name()))
				.collect(Collectors.toList());

		userDetails.setAuthorities(authorities);
		
		return userDetails;
	}
	
	
	@Override
	public Boolean isLoginCorrect(LoginDTO credentials) {
		User logInUser = dao.findByLoginAndPassword(credentials.getLogin(), credentials.getPassword());
		return logInUser != null;
	}
	
	
	@Override
	public void rewind(Long id) throws NoVersionFoundException {
		versionService.rewind(id, dao);
	}
	
	private BlogUserDetails mapUserDetails(User user){
		return mapper.map(user, BlogUserDetails.class);
	}

	private User map(UserEditDTO userDTO) {
		User user = mapper.map(userDTO, User.class);
		
		if(StringUtils.isEmpty(user.getPassword())) {
			user = setOriginalPassword(user);
		}
		return user;
	}
	
	private User setOriginalPassword(User user) {
		Optional<User> originalUser = Optional.ofNullable(dao.findOne(user.getId()));
		
		if(originalUser.isPresent()) {
			user.setPassword(originalUser.get().getPassword());
		}
		return user;
	} 

	private UserEditDTO map(User user) {
		return mapper.map(user, UserEditDTO.class);
	}

	private UserInfoDTO mapInfo(User user) {
		return mapper.map(user, UserInfoDTO.class);
	}
}
