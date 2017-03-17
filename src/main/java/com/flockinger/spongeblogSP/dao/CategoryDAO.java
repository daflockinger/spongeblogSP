package com.flockinger.spongeblogSP.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.Category;

@Repository
public interface CategoryDAO extends PagingAndSortingRepository<Category,Long>, CategoryDAOCustom{
	Category findByName(String name);
	List<Category> findByParentId(Long parentId);
}
