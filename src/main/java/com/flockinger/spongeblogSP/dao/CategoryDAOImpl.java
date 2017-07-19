package com.flockinger.spongeblogSP.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.spongeblogSP.model.Category;


public class CategoryDAOImpl implements CategoryDAOCustom {

  @Autowired
  EntityManager em;

  @Override
  public Category update(Category category) {
    if (isParentValid(category.getParent())) {
      category.setParent(em.find(Category.class, category.getParent().getId()));
    }
    category.setSubCategories(getPersistedChildren(category.getId()));

    return em.merge(category);
  }

  private boolean isParentValid(Category parent) {
    return parent != null && parent.getId() != null && isCategoryExisting(parent.getId());
  }

  private boolean isCategoryExisting(Long id) {
    return em.find(Category.class, id) != null;
  }

  private List<Category> getPersistedChildren(Long categoryId) {
    Category category = em.find(Category.class, categoryId);

    return category.getSubCategories();
  }
}
