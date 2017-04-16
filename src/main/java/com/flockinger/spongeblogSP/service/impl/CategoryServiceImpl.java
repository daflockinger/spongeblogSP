package com.flockinger.spongeblogSP.service.impl;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
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
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.exception.OrphanedDependingEntitiesException;
import com.flockinger.spongeblogSP.model.Category;
import com.flockinger.spongeblogSP.service.CategoryService;
import com.flockinger.spongeblogSP.service.VersioningService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryDAO dao;
	
	private ModelMapper mapper;
	
	@Autowired
	private VersioningService<Category,CategoryDAO> versionService;
	
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
		mapper.addMappings(new PropertyMap<Category, CategoryPostsDTO>() {
			@Override
			protected void configure() {
				map().setParent(source.getParent().getId());
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
	public void deleteCategory(Long id) throws EntityIsNotExistingException, 
	OrphanedDependingEntitiesException{
		if(!dao.exists(id)){
			throw new EntityIsNotExistingException("Category");
		}
		if(CollectionUtils.isNotEmpty(dao.findOne(id).getPosts())){
			throw new OrphanedDependingEntitiesException("Posts still connected to category."
					+ "Please change category of posts before deletion!");
		}
		
		dao.delete(id);
	}
	
	@Override
	public void rewind(Long id) throws NoVersionFoundException {
		versionService.rewind(id, dao);
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
