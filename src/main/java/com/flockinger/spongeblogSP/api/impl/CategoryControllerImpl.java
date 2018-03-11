package com.flockinger.spongeblogSP.api.impl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.CategoryController;
import com.flockinger.spongeblogSP.api.util.RequestValidator;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.exception.OrphanedDependingEntitiesException;
import com.flockinger.spongeblogSP.service.CategoryService;

import io.swagger.annotations.ApiParam;

@RestController
public class CategoryControllerImpl implements CategoryController {

  @Autowired
  private CategoryService service;

  @Autowired
  private RequestValidator validator;

  public ResponseEntity<?> apiV1CategoriesCategoryIdDelete(
      @ApiParam(value = "Unique identifier of a Category;",
          required = true) @PathVariable("categoryId") Long categoryId)
      throws EntityIsNotExistingException, OrphanedDependingEntitiesException {

    service.deleteCategory(categoryId);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  public ResponseEntity<CategoryDTO> apiV1CategoriesCategoryIdGet(
      @ApiParam(value = "Unique identifier of a Category;",
          required = true) @PathVariable("categoryId") Long categoryId)
      throws EntityIsNotExistingException {
    return new ResponseEntity<CategoryDTO>(service.getCategory(categoryId), HttpStatus.OK);
  }

  public ResponseEntity<?> apiV1CategoriesChildrenParentCategoryIdGet(
      @ApiParam(value = "Unique identifier of the parent Category;",
          required = true) @PathVariable("parentCategoryId") Long parentCategoryId)
      throws EntityIsNotExistingException {

    List<CategoryDTO> children = service.getCategoriesFromParent(parentCategoryId);
    return new ResponseEntity<List<CategoryDTO>>(children, HttpStatus.OK);
  }

  public ResponseEntity<?> apiV1CategoriesGet() {

    List<CategoryDTO> categories = service.getAllCategories();
    return new ResponseEntity<List<CategoryDTO>>(categories, HttpStatus.OK);
  }

  public ResponseEntity<?> apiV1CategoriesPost(
      @ApiParam(value = "", required = true) @Valid @RequestBody CategoryDTO categoryEdit,
      BindingResult bindingResult)
      throws DtoValidationFailedException, DependencyNotFoundException {

    validator.validateRequestBody(bindingResult);
    CategoryDTO createdCategory = service.createCategory(categoryEdit);
    return new ResponseEntity<CategoryDTO>(createdCategory, HttpStatus.CREATED);
  }

  public ResponseEntity<?> apiV1CategoriesPut(
      @ApiParam(value = "", required = true) @Valid @RequestBody CategoryDTO categoryEdit,
      BindingResult bindingResult) throws EntityIsNotExistingException,
      DtoValidationFailedException, DependencyNotFoundException {

    validator.validateRequestBody(bindingResult);
    validator.assertIdForUpdate(categoryEdit.getCategoryId());
    service.updateCategory(categoryEdit);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  public ResponseEntity<?> apiV1CategoriesRewindCategoryIdPut(
      @ApiParam(value = "Unique identifier of a Category;",
          required = true) @PathVariable("categoryId") Long categoryId)
      throws NoVersionFoundException {
    service.rewind(categoryId);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }
}
