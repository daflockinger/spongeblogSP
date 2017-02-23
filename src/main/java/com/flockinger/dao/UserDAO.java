package com.flockinger.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.model.User;

@Repository
public interface UserDAO extends PagingAndSortingRepository<User, Long>{
	User findByLogin(String login);
}
