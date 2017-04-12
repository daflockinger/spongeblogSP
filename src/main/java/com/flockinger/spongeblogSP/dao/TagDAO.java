package com.flockinger.spongeblogSP.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.Tag;

@Repository
public interface TagDAO extends PagingAndSortingRepository<Tag,Long>, RevisionRepository<Tag, Long, Integer>{
	Tag findByName(String name);
}
