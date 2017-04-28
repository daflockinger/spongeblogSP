package com.flockinger.spongeblogSP.api.impl;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


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
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


	public ResponseEntity<?> apiV1BlogDelete() throws EntityIsNotExistingException {
		
		service.deleteBlog();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1BlogGet() throws EntityIsNotExistingException {
		return new ResponseEntity<BlogDTO>(addSelfLink(service.getBlog()), HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1BlogPost(@ApiParam(value = "", required = true) @Valid @RequestBody BlogDTO blogEdit,
			BindingResult bindingResult) throws DtoValidationFailedException, DuplicateEntityException {
		
		validator.validateRequestBody(bindingResult);
		BlogDTO createdBlog = service.createBlog(blogEdit);
		return new ResponseEntity<BlogDTO>(addSelfLink(createdBlog), HttpStatus.CREATED);
	}

	public ResponseEntity<?> apiV1BlogPut(@ApiParam(value = "", required = true) @Valid @RequestBody BlogDTO blogEdit,
			BindingResult bindingResult) throws DtoValidationFailedException, EntityIsNotExistingException {
		
		validator.validateRequestBody(bindingResult);
		service.updateBlog(blogEdit);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1BlogRewindPut() throws NoVersionFoundException {
		
		service.rewind(null);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	
	private BlogDTO addSelfLink(BlogDTO blog) {
		try {
			blog.add(linkTo(methodOn(BlogControllerImpl.class).apiV1BlogGet()).withSelfRel());
		} catch (EntityIsNotExistingException e) {
			logger.error("Not found after Persisting. Should not happen.");
		}
		return blog;
	}
}
