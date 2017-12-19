package com.flockinger.spongeblogSP.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostsPage;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;

import io.swagger.annotations.*;

public interface PageController {
  @ApiOperation(value = "All pages with no category.",
      notes = "Returns all pages having no category assigned.", response = PostsPage.class,
      tags = {"Pages",})
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Page links.", response = PostsPage.class),
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

  @RequestMapping(value = "/api/v1/pages", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.GET)
  ResponseEntity<PostsPage> apiV1PagesGetUsingGET(
      @ApiParam(value = "Filter pages without a category.", defaultValue = "false") @RequestParam(
          value = "without-category", required = false,
          defaultValue = "false") Boolean withoutCategory);


  @ApiOperation(value = "Update Page", notes = "Updated a Page entry.", response = Object.class,
      tags = {"Pages",})
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Page updated.", response = Object.class),
          @ApiResponse(code = 201, message = "Created", response = Void.class),
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

  @RequestMapping(value = "/api/v1/pages", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.PUT)
  ResponseEntity<Object> apiV1PagesPutUsingPUT(
      @ApiParam(value = "pageEdit", required = true) @Valid @RequestBody PostDTO pageEdit,
      BindingResult bindingResult) throws DtoValidationFailedException,
      EntityIsNotExistingException, DuplicateEntityException, DependencyNotFoundException;

  @ApiOperation(value = "Delete Page", notes = "Deletes a Page with defined Id.",
      response = Object.class, tags = {"Pages",})
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Page deleted.", response = Object.class),
          @ApiResponse(code = 204, message = "No Content", response = Void.class),
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

  @RequestMapping(value = "/api/v1/pages/{pageId}", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.DELETE)
  ResponseEntity<Object> apiV1PostsPageIdDeleteUsingDELETE(
      @ApiParam(value = "Unique identifier of a Page;",
          required = true) @PathVariable("pageId") Long pageId)
      throws EntityIsNotExistingException;


  @ApiOperation(value = "Get Page", notes = "Fetches Page with defined Id.",
      response = PostDTO.class, tags = {"Pages",})
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Page with id.", response = PostDTO.class),
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

  @RequestMapping(value = "/api/v1/pages/{pageId}", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.GET)
  ResponseEntity<PostDTO> apiV1PostsPageIdGetUsingGET(
      @ApiParam(value = "Unique identifier of a Page;",
          required = true) @PathVariable("pageId") Long pageId)
      throws EntityIsNotExistingException;


  @ApiOperation(value = "Create Page", notes = "Creates new Page entry.", response = PostDTO.class,
      tags = {"Pages",})
  @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = PostDTO.class),
      @ApiResponse(code = 201, message = "Created Page.", response = PostDTO.class),
      @ApiResponse(code = 400, message = "Bad request (validation failed).",
          response = Error.class),
      @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).",
          response = Void.class),
      @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).",
          response = Void.class),
      @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
      @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class)})

  @RequestMapping(value = "/api/v1/pages", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.POST)
  ResponseEntity<PostDTO> apiV1PostsPageUsingPOST(
      @ApiParam(value = "pageEdit", required = true) @Valid @RequestBody PostDTO pageEdit,
      BindingResult bindingResult)
      throws DuplicateEntityException, DependencyNotFoundException, DtoValidationFailedException;

}
