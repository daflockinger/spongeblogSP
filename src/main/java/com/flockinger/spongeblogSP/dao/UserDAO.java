package com.flockinger.spongeblogSP.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.User;

@Repository
public interface UserDAO extends PagingAndSortingRepository<User, Long>, RevisionRepository<User, Long, Integer>{
	User findByLogin(String login);
}
