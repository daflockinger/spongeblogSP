package com.flockinger.spongeblogSP.service;

import java.util.List;

import com.flockinger.spongeblogSP.dao.CategoryDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.CategoryPostsDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;

public interface CategoryService {

	List<CategoryDTO> getAllCategories();
	
	List<CategoryPostsDTO> getCategoriesFromParent(Long parentId) throws EntityIsNotExistingException;
	
	CategoryPostsDTO getCategory(Long id) throws EntityIsNotExistingException;
	
	CategoryDTO createCategory(CategoryDTO category) throws DuplicateEntityException;
	
	void updateCategory(CategoryDTO tag) throws EntityIsNotExistingException;
	
	void deleteCategory(Long id) throws EntityIsNotExistingException;

}