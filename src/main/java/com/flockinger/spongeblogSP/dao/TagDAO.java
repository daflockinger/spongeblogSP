package com.flockinger.spongeblogSP.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.Tag;

@Repository
public interface TagDAO extends PagingAndSortingRepository<Tag,Long>, VersionDAO<Tag>{
	Tag findByName(String name);
}
