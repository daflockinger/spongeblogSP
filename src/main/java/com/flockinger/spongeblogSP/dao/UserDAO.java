package com.flockinger.spongeblogSP.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.User;

@Repository
public interface UserDAO extends PagingAndSortingRepository<User, Long>, VersionDAO<User>{
	User findByLogin(String login);
	User findByEmail(String email);
	User findByLoginAndPassword(String login, String password);
}
