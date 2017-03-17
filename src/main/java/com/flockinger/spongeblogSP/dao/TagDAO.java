package com.flockinger.spongeblogSP.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.Tag;
import com.flockinger.spongeblogSP.model.User;

@Repository
public interface TagDAO extends PagingAndSortingRepository<Tag,Long>{
	Tag findByName(String name);
}
