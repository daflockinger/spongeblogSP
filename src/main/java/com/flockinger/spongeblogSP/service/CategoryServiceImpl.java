package com.flockinger.spongeblogSP.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.dao.CategoryDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.CategoryPostsDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.model.Category;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryDAO dao;
	
	private ModelMapper mapper;
	
	@Autowired
	public CategoryServiceImpl(ModelMapper mapper) {
		this.mapper = mapper;
		mapper.addMappings(new PropertyMap<CategoryDTO, Category>() {
			@Override
			protected void configure() {
				map(source.getParentId(),destination.getParent().getId());
			}
		});
		mapper.addMappings(new PropertyMap<Category, CategoryDTO>() {
			@Override
			protected void configure() {
				map(source.getParent().getId(),destination.getParentId());
			}
		});
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryDTO> getAllCategories() {
		List<CategoryDTO> tagDTOs = new ArrayList<>();
		Iterable<Category> tags = dao.findAll();

		tags.iterator().forEachRemaining(tag -> tagDTOs.add(map(tag)));

		return tagDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CategoryPostsDTO> getCategoriesFromParent(Long parentId) throws EntityIsNotExistingException{
		List<Category> categories = dao.findByParentId(parentId);

		return categories.stream().map(category -> mapToPostDto(category)).collect(Collectors.toList());
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public CategoryPostsDTO getCategory(Long id) throws EntityIsNotExistingException{
		if(!dao.exists(id)){
			throw new EntityIsNotExistingException("Category");
		}
		
		return mapToPostDto(dao.findOne(id));
	}

	@Override
	@Transactional
	public CategoryDTO createCategory(CategoryDTO category) throws DuplicateEntityException {
		if(isCategoryExistingAlready(category.getName())){
			throw new DuplicateEntityException("Category");
		}				
		return map(dao.save(map(category)));
	}
	
	private boolean isCategoryExistingAlready(String name) {		
		return isNotEmpty(name) && dao.findByName(name) != null;
	}

	@Override
	@Transactional
	public void updateCategory(CategoryDTO category) throws EntityIsNotExistingException {
		if(!dao.exists(category.getId())){
			throw new EntityIsNotExistingException("Category");
		}
		
		dao.update(map(category));
	}

	@Override
	@Transactional
	public void deleteCategory(Long id) throws EntityIsNotExistingException{
		if(!dao.exists(id)){
			throw new EntityIsNotExistingException("Category");
		}
		
		dao.delete(id);
	}
	
	private CategoryPostsDTO mapToPostDto(Category tag) {
		return mapper.map(tag, CategoryPostsDTO.class);
	}
	
	private Category map(CategoryDTO tagDto) {
		return mapper.map(tagDto, Category.class);
	}

	private CategoryDTO map(Category tag) {
		return mapper.map(tag, CategoryDTO.class);
	}
}
