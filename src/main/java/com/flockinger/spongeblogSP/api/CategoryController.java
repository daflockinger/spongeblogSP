package com.flockinger.spongeblogSP.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.Error;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.exception.OrphanedDependingEntitiesException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface CategoryController {

  @ApiOperation(value = "Delete Category", notes = "Deletes a Category with defined Id.",
      response = Void.class, tags = {"Categories",})
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Category deleted.", response = Void.class),
          @ApiResponse(code = 400, message = "Bad request (validation failed).",
              response = Error.class),
          @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).",
              response = Void.class),
          @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).",
              response = Void.class),
          @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
          @ApiResponse(code = 409, message = "Request results in a conflict.",
              response = Error.class),
          @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class)})
  @RequestMapping(value = "/api/v1/categories/{categoryId}", produces = {"application/json"},
      method = RequestMethod.DELETE)
  ResponseEntity<?> apiV1CategoriesCategoryIdDelete(
      @ApiParam(value = "Unique identifier of a Category;",
          required = true) @PathVariable("categoryId") Long categoryId)
      throws EntityIsNotExistingException, OrphanedDependingEntitiesException;


  @ApiOperation(value = "Get Category", notes = "Fetches Category with defined Id.",
      response = CategoryDTO.class, tags = {"Categories",})
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Category with id.", response = CategoryDTO.class),
      @ApiResponse(code = 400, message = "Bad request (validation failed).",
          response = Error.class),
      @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).",
          response = Void.class),
      @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).",
          response = Void.class),
      @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
      @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class)})
  @RequestMapping(value = "/api/v1/categories/{categoryId}", produces = {"application/json"},
      method = RequestMethod.GET)
  ResponseEntity<?> apiV1CategoriesCategoryIdGet(
      @ApiParam(value = "Unique identifier of a Category;",
          required = true) @PathVariable("categoryId") Long categoryId)
      throws EntityIsNotExistingException;


  @ApiOperation(value = "Categorys of Parent.",
      notes = "Returns all Categorys of defined parent Category.", response = CategoryDTO.class,
      responseContainer = "List", tags = {"Categories",})
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Categories.", response = CategoryDTO.class),
          @ApiResponse(code = 400, message = "Bad request (validation failed).",
              response = Error.class),
          @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).",
              response = Void.class),
          @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).",
              response = Void.class),
          @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
          @ApiResponse(code = 409, message = "Request results in a conflict.",
              response = Error.class),
          @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class)})
  @RequestMapping(value = "/api/v1/categories/children/{parentCategoryId}",
      produces = {"application/json"}, method = RequestMethod.GET)
  ResponseEntity<?> apiV1CategoriesChildrenParentCategoryIdGet(
      @ApiParam(value = "Unique identifier of the parent Category;",
          required = true) @PathVariable("parentCategoryId") Long parentCategoryId)
      throws EntityIsNotExistingException;


  @ApiOperation(value = "All Categorys", notes = "Returns all Categorys.",
      response = CategoryDTO.class, responseContainer = "List", tags = {"Categories",})
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Categories.", response = CategoryDTO.class),
          @ApiResponse(code = 400, message = "Bad request (validation failed).",
              response = Error.class),
          @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).",
              response = Void.class),
          @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).",
              response = Void.class),
          @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
          @ApiResponse(code = 409, message = "Request results in a conflict.",
              response = Error.class),
          @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class)})
  @RequestMapping(value = "/api/v1/categories", produces = {"application/json"},
      method = RequestMethod.GET)
  ResponseEntity<?> apiV1CategoriesGet();


  @ApiOperation(value = "Create Category", notes = "Creates new Category entry.",
      response = CategoryDTO.class, tags = {"Categories",})
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Created Category.", response = CategoryDTO.class),
      @ApiResponse(code = 400, message = "Bad request (validation failed).",
          response = Error.class),
      @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).",
          response = Void.class),
      @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).",
          response = Void.class),
      @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
      @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class)})
  @RequestMapping(value = "/api/v1/categories", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.POST)
  ResponseEntity<?> apiV1CategoriesPost(
      @ApiParam(value = "", required = true) @RequestBody CategoryDTO categoryEdit,
      BindingResult bindingResult) throws DtoValidationFailedException, DependencyNotFoundException;


  @ApiOperation(value = "Update Category", notes = "Updated a Category entry.",
      response = Void.class, tags = {"Categories",})
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Category updated.", response = Void.class),
          @ApiResponse(code = 400, message = "Bad request (validation failed).",
              response = Error.class),
          @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).",
              response = Void.class),
          @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).",
              response = Void.class),
          @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
          @ApiResponse(code = 409, message = "Request results in a conflict.",
              response = Error.class),
          @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class)})
  @RequestMapping(value = "/api/v1/categories", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.PUT)
  ResponseEntity<?> apiV1CategoriesPut(
      @ApiParam(value = "", required = true) @RequestBody CategoryDTO categoryEdit,
      BindingResult bindingResult) throws EntityIsNotExistingException,
      DtoValidationFailedException, DependencyNotFoundException;


  @ApiOperation(value = "Rewind Category", notes = "Restores previous Category entry.",
      response = Void.class, tags = {"Categories",})
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Previous Category restored.", response = Void.class),
      @ApiResponse(code = 400, message = "Bad request (validation failed).",
          response = Error.class),
      @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).",
          response = Void.class),
      @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).",
          response = Void.class),
      @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
      @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class)})
  @RequestMapping(value = "/api/v1/categories/rewind/{categoryId}", produces = {"application/json"},
      method = RequestMethod.PUT)
  ResponseEntity<?> apiV1CategoriesRewindCategoryIdPut(
      @ApiParam(value = "Unique identifier of a Category;",
          required = true) @PathVariable("categoryId") Long categoryId)
      throws NoVersionFoundException;

}
