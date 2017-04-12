package com.flockinger.spongeblogSP.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.Category;

@Repository
public interface CategoryDAO extends PagingAndSortingRepository<Category,Long>, CategoryDAOCustom, RevisionRepository<Category, Long, Integer>{
	Category findByName(String name);
	List<Category> findByParentId(Long parentId);
}
