package com.flockinger.spongeblogSP.api.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.CategoryController;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.exception.OrphanedDependingEntitiesException;
import com.flockinger.spongeblogSP.service.CategoryService;

import io.swagger.annotations.ApiParam;

@RestController
public class CategoryControllerImpl implements CategoryController {

	@Autowired
	private CategoryService service;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<?> apiV1CategoriesCategoryIdDelete(
			@ApiParam(value = "Unique identifier of a Category;", required = true) @PathVariable("categoryId") Long categoryId)
			throws EntityIsNotExistingException, OrphanedDependingEntitiesException {

		service.deleteCategory(categoryId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<CategoryDTO> apiV1CategoriesCategoryIdGet(
			@ApiParam(value = "Unique identifier of a Category;", required = true) @PathVariable("categoryId") Long categoryId)
			throws EntityIsNotExistingException {
		return new ResponseEntity<CategoryDTO>(addSelfAndParentLink(service.getCategory(categoryId)), HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1CategoriesChildrenParentCategoryIdGet(
			@ApiParam(value = "Unique identifier of the parent Category;", required = true) @PathVariable("parentCategoryId") Long parentCategoryId)
			throws EntityIsNotExistingException {

		List<CategoryDTO> children = service.getCategoriesFromParent(parentCategoryId);
		children.forEach(cat -> addSelfAndParentLink(cat));
		return new ResponseEntity<List<CategoryDTO>>(children, HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1CategoriesGet() {

		List<CategoryDTO> categories = service.getAllCategories();
		categories.forEach(cat -> addSelfAndParentLink(cat));
		return new ResponseEntity<List<CategoryDTO>>(categories, HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1CategoriesPost(
			@ApiParam(value = "", required = true) @Valid @RequestBody CategoryDTO categoryEdit,
			BindingResult bindingResult) throws DtoValidationFailedException, DependencyNotFoundException {

		validate(bindingResult);
		CategoryDTO createdCategory = service.createCategory(categoryEdit);
		return new ResponseEntity<CategoryDTO>(addSelfAndParentLink(createdCategory), HttpStatus.CREATED);
	}

	public ResponseEntity<?> apiV1CategoriesPut(
			@ApiParam(value = "", required = true) @Valid @RequestBody CategoryDTO categoryEdit,
			BindingResult bindingResult) throws EntityIsNotExistingException, DtoValidationFailedException, DependencyNotFoundException {
		
		validate(bindingResult);
		service.updateCategory(categoryEdit);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1CategoriesRewindCategoryIdPut(
			@ApiParam(value = "Unique identifier of a Category;", required = true) @PathVariable("categoryId") Long categoryId) throws NoVersionFoundException {
		service.rewind(categoryId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	private void validate(BindingResult bindingResult) throws DtoValidationFailedException {
		if (bindingResult.hasErrors()) {
			throw new DtoValidationFailedException("Invalid field entries!", bindingResult.getFieldErrors());
		}
	}

	private CategoryDTO addSelfAndParentLink(CategoryDTO category) {
		try {
			category.add(linkTo(methodOn(CategoryControllerImpl.class, category.getCategoryId())
					.apiV1CategoriesCategoryIdGet(category.getCategoryId())).withSelfRel());

			if (category.getParentId() != null) {
				category.add(linkTo(
						methodOn(CategoryControllerImpl.class).apiV1CategoriesCategoryIdGet(category.getParentId()))
								.withRel("parent"));
			}
		} catch (EntityIsNotExistingException e) {
			logger.error("Not found after Persisting. Should not happen.");
		}
		return category;
	}
}
