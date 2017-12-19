package com.flockinger.spongeblogSP.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.dao.CategoryDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.exception.OrphanedDependingEntitiesException;
import com.flockinger.spongeblogSP.model.Category;
import com.flockinger.spongeblogSP.service.CategoryService;
import com.flockinger.spongeblogSP.service.VersioningService;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  private CategoryDAO dao;

  private ModelMapper mapper;

  @Autowired
  private VersioningService<Category, CategoryDAO> versionService;

  @Autowired
  public CategoryServiceImpl(ModelMapper mapper) {
    this.mapper = mapper;
    mapper.addMappings(new PropertyMap<CategoryDTO, Category>() {
      @Override
      protected void configure() {
        map().setId(source.getCategoryId());
        map(source.getParentId(), destination.getParent().getId());
      }
    });
    mapper.addMappings(new PropertyMap<Category, CategoryDTO>() {
      @Override
      protected void configure() {
        map().setCategoryId(source.getId());
        map(source.getParent().getId(), destination.getParentId());
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
  public List<CategoryDTO> getCategoriesFromParent(Long parentId)
      throws EntityIsNotExistingException {
    List<Category> categories = dao.findByParentId(parentId);

    return categories.stream().map(category -> map(category)).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryDTO getCategory(Long id) throws EntityIsNotExistingException {
    if (!dao.exists(id)) {
      throw new EntityIsNotExistingException("Category");
    }
    return map(dao.findOne(id));
  }

  @Override
  @Transactional
  public CategoryDTO createCategory(CategoryDTO category) throws DependencyNotFoundException {
    if (isParentCategoryExisting(category)) {
      throw new DependencyNotFoundException(
          "Parent of Category not found with id" + category.getParentId());
    }
    return map(dao.save(map(category)));
  }

  private boolean isParentCategoryExisting(CategoryDTO category) {
    return category.getParentId() != null && !dao.exists(category.getParentId());
  }

  @Override
  @Transactional
  public void updateCategory(CategoryDTO category)
      throws EntityIsNotExistingException, DependencyNotFoundException {
    if (!dao.exists(category.getCategoryId())) {
      throw new EntityIsNotExistingException("Category");
    }
    if (isParentCategoryExisting(category)) {
      throw new DependencyNotFoundException(
          "Parent of Category not found with id" + category.getParentId());
    }

    dao.update(map(category));
  }

  @Override
  @Transactional
  public void deleteCategory(Long id)
      throws EntityIsNotExistingException, OrphanedDependingEntitiesException {
    if (!dao.exists(id)) {
      throw new EntityIsNotExistingException("Category");
    }
    if (CollectionUtils.isNotEmpty(dao.findOne(id).getPosts())) {
      throw new OrphanedDependingEntitiesException("Posts still connected to category."
          + "Please change category of posts before deletion!");
    }

    dao.delete(id);
  }

  @Override
  public void rewind(Long id) throws NoVersionFoundException {
    versionService.rewind(id, dao);
  }

  private Category map(CategoryDTO categoryDto) {
    return mapper.map(categoryDto, Category.class);
  }

  private CategoryDTO map(Category category) {
    CategoryDTO categoryDto = mapper.map(category, CategoryDTO.class);
    categoryDto.setChildren(ListUtils.emptyIfNull(category.getSubCategories())
          .stream()
          .map(child -> mapper.map(child, CategoryDTO.class))
          .collect(Collectors.toList()));
    return categoryDto;
  }
}
