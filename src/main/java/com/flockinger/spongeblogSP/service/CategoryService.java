package com.flockinger.spongeblogSP.service;

import java.util.List;

import com.flockinger.spongeblogSP.dao.CategoryDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.CategoryPostsDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.OrphanedDependingEntitiesException;

public interface CategoryService extends Versionable {

	List<CategoryDTO> getAllCategories();
	
	List<CategoryDTO> getCategoriesFromParent(Long parentId) throws EntityIsNotExistingException;
	
	CategoryDTO getCategory(Long id) throws EntityIsNotExistingException;
	
	CategoryDTO createCategory(CategoryDTO category) throws DependencyNotFoundException;
	
	void updateCategory(CategoryDTO tag) throws EntityIsNotExistingException,DependencyNotFoundException;
	
	void deleteCategory(Long id) throws EntityIsNotExistingException, OrphanedDependingEntitiesException;

}