package com.flockinger.spongeblogSP.api.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.BlogController;
import com.flockinger.spongeblogSP.api.util.RequestValidator;
import com.flockinger.spongeblogSP.dto.BlogDTO;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.service.BlogService;

import io.swagger.annotations.ApiParam;

@RestController
public class BlogControllerImpl implements BlogController {

  @Autowired
  private BlogService service;

  @Autowired
  private RequestValidator validator;


  public ResponseEntity<Void> apiV1BlogDelete() throws EntityIsNotExistingException {

    service.deleteBlog();
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  public ResponseEntity<BlogDTO> apiV1BlogGet() throws EntityIsNotExistingException {
    return new ResponseEntity<BlogDTO>(service.getBlog(), HttpStatus.OK);
  }

  public ResponseEntity<BlogDTO> apiV1BlogPost(
      @ApiParam(value = "", required = true) @Valid @RequestBody BlogDTO blogEdit,
      BindingResult bindingResult) throws DtoValidationFailedException, DuplicateEntityException {

    validator.validateRequestBody(bindingResult);
    BlogDTO createdBlog = service.createBlog(blogEdit);
    return new ResponseEntity<BlogDTO>(createdBlog, HttpStatus.CREATED);
  }

  public ResponseEntity<Void> apiV1BlogPut(
      @ApiParam(value = "", required = true) @Valid @RequestBody BlogDTO blogEdit,
      BindingResult bindingResult)
      throws DtoValidationFailedException, EntityIsNotExistingException {

    validator.validateRequestBody(bindingResult);
    service.updateBlog(blogEdit);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  public ResponseEntity<Void> apiV1BlogRewindPut() throws NoVersionFoundException {
    service.rewind(null);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }
}
