package com.flockinger.spongeblogSP.api.impl;

import static com.flockinger.spongeblogSP.config.BlogConfigurationConstants.PAGING_DEFAULT_ITEMS_PER_PAGE_KEY;
import static com.flockinger.spongeblogSP.config.BlogConfigurationConstants.PAGING_DEFAULT_PAGE_KEY;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.PostController;
import com.flockinger.spongeblogSP.api.util.RequestValidator;
import com.flockinger.spongeblogSP.dto.Paging;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostPreviewDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.enums.PostStatus;
import com.flockinger.spongeblogSP.service.PostService;

import io.swagger.annotations.ApiParam;

@RestController
public class PostControllerImpl implements PostController {

	@Autowired
	private PostService service;
	
	@Autowired
	private RequestValidator validator;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseEntity<?> apiV1PostsAuthorUserIdGet(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId,
			@ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false, defaultValue = PAGING_DEFAULT_PAGE_KEY) Integer page,
			@ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false, defaultValue = PAGING_DEFAULT_ITEMS_PER_PAGE_KEY) Integer size)
			throws DtoValidationFailedException {

		validator.assertPaging(page, size);
		List<PostPreviewDTO> posts = service.getPostsFromAuthorId(userId, new Paging(page, getSortingByDateDesc(), size));
		posts.forEach(post -> addSelfLink(post));
		return new ResponseEntity<List<PostPreviewDTO>>(posts, HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsAuthorUserIdStatusGet(
			@ApiParam(value = "Unique identifier of a User;", required = true) @PathVariable("userId") Long userId,
			@ApiParam(value = "Post Status Id", required = true) @PathVariable("status") String status,
			@ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false, defaultValue = PAGING_DEFAULT_PAGE_KEY) Integer page,
			@ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false, defaultValue = PAGING_DEFAULT_ITEMS_PER_PAGE_KEY) Integer size)
			throws DtoValidationFailedException {

		validator.assertPaging(page, size);
		validator.assertStatus(PostStatus.class,status);
		List<PostPreviewDTO> posts = service.getPostsFromAuthorIdWithStatus(userId, PostStatus.valueOf(status),
				new Paging(page, getSortingByDateDesc(), size));
		posts.forEach(post -> addSelfLink(post));
		return new ResponseEntity<List<PostPreviewDTO>>(posts, HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsCategoryCategoryIdGet(
			@ApiParam(value = "Unique identifier of a Category;", required = true) @PathVariable("categoryId") Long categoryId,
			@ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false, defaultValue = PAGING_DEFAULT_PAGE_KEY) Integer page,
			@ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false, defaultValue = PAGING_DEFAULT_ITEMS_PER_PAGE_KEY) Integer size) throws DtoValidationFailedException {
		
		validator.assertPaging(page,size);
		List<PostPreviewDTO> posts = service.getPostsFromCategoryId(categoryId, new Paging(page,getSortingByDateDesc(),size));
		posts.forEach(post -> addSelfLink(post));
		return new ResponseEntity<List<PostPreviewDTO>>(posts, HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsCategoryCategoryIdStatusGet(
			@ApiParam(value = "Unique identifier of a Category;", required = true) @PathVariable("categoryId") Long categoryId,
			@ApiParam(value = "Post Status Id", required = true) @PathVariable("status") String status,
			@ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false, defaultValue = PAGING_DEFAULT_PAGE_KEY) Integer page,
			@ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false, defaultValue = PAGING_DEFAULT_ITEMS_PER_PAGE_KEY) Integer size) throws DtoValidationFailedException {
		
		validator.assertPaging(page, size);
		validator.assertStatus(PostStatus.class,status);
		List<PostPreviewDTO> posts = service.getPostsFromCategoryIdWithStatus(categoryId, PostStatus.valueOf(status), new Paging(page,getSortingByDateDesc(),size));
		posts.forEach(post -> addSelfLink(post));
		return new ResponseEntity<List<PostPreviewDTO>>(posts, HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsTagTagIdGet(
			@ApiParam(value = "Unique identifier of a Tag;", required = true) @PathVariable("tagId") Long tagId,
			@ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false, defaultValue = PAGING_DEFAULT_PAGE_KEY) Integer page,
			@ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false, defaultValue = PAGING_DEFAULT_ITEMS_PER_PAGE_KEY) Integer size) throws DtoValidationFailedException {
		
		validator.assertPaging(page, size);
		List<PostPreviewDTO> posts = service.getPostsFromTagId(tagId, new Paging(page,getSortingByDateDesc(),size));
		posts.forEach(post -> addSelfLink(post));
		return new ResponseEntity<List<PostPreviewDTO>>(posts, HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsTagTagIdStatusGet(
			@ApiParam(value = "Unique identifier of a Tag;", required = true) @PathVariable("tagId") Long tagId,
			@ApiParam(value = "Post Status Id", required = true) @PathVariable("status") String status,
			@ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false, defaultValue = PAGING_DEFAULT_PAGE_KEY) Integer page,
			@ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false, defaultValue = PAGING_DEFAULT_ITEMS_PER_PAGE_KEY) Integer size) throws DtoValidationFailedException {
		
		validator.assertPaging(page, size);
		validator.assertStatus(PostStatus.class,status);
		List<PostPreviewDTO> posts = service.getPostsFromTagIdWithStatus(tagId, PostStatus.valueOf(status), new Paging(page,getSortingByDateDesc(),size));
		posts.forEach(post -> addSelfLink(post));
		return new ResponseEntity<List<PostPreviewDTO>>(posts, HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsGet(
			@ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false, defaultValue = PAGING_DEFAULT_PAGE_KEY) Integer page,
			@ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false, defaultValue = PAGING_DEFAULT_ITEMS_PER_PAGE_KEY) Integer size)
			throws DtoValidationFailedException {

		validator.assertPaging(page, size);
		List<PostPreviewDTO> posts = service.getAllPosts(new Paging(page, getSortingByDateDesc(), size));
		posts.forEach(post -> addSelfLink(post));
		return new ResponseEntity<List<PostPreviewDTO>>(posts, HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsPost(@ApiParam(value = "", required = true) @Valid @RequestBody PostDTO postEdit,
			BindingResult bindingResult)
			throws DtoValidationFailedException, DuplicateEntityException, DependencyNotFoundException {

		validator.validateRequestBody(bindingResult);
		PostDTO createdPost = service.createPost(postEdit);
		return new ResponseEntity<PostDTO>(addSelfLinkToPost(createdPost), HttpStatus.CREATED);
	}

	public ResponseEntity<?> apiV1PostsPostIdDelete(
			@ApiParam(value = "Unique identifier of a Post;", required = true) @PathVariable("postId") Long postId)
			throws EntityIsNotExistingException {

		service.deletePost(postId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsPostIdGet(
			@ApiParam(value = "Unique identifier of a Post;", required = true) @PathVariable("postId") Long postId)
			throws EntityIsNotExistingException {

		PostDTO post = addSelfLinkToPost(service.getPost(postId));
		return new ResponseEntity<PostDTO>(post, HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsPut(@ApiParam(value = "", required = true) @Valid @RequestBody PostDTO postEdit,
			BindingResult bindingResult) throws DtoValidationFailedException, EntityIsNotExistingException, DuplicateEntityException, DependencyNotFoundException {
		
		validator.validateRequestBody(bindingResult);
		validator.assertIdForUpdate(postEdit.getPostId());
		service.updatePost(postEdit);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsRewindPostIdPut(
			@ApiParam(value = "Unique identifier of a Post;", required = true) @PathVariable("postId") Long postId) throws NoVersionFoundException {
		
		service.rewind(postId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1PostsStatusStatusGet(
			@ApiParam(value = "Post Status Id", required = true) @PathVariable("status") String status,
			@ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false, defaultValue = PAGING_DEFAULT_PAGE_KEY) Integer page,
			@ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false, defaultValue = PAGING_DEFAULT_ITEMS_PER_PAGE_KEY) Integer size) throws DtoValidationFailedException {
		
		validator.assertPaging(page, size);
		validator.assertStatus(PostStatus.class,status);
		List<PostPreviewDTO> posts = service.getAllPostsWithStatus(PostStatus.valueOf(status), new Paging(page,getSortingByDateDesc(),size));
		posts.forEach(post -> addSelfLink(post));
		return new ResponseEntity<List<PostPreviewDTO>>(posts, HttpStatus.OK);
	}
	

	private PostDTO addSelfLinkToPost(PostDTO post) {
		try {
			post.add(linkTo(methodOn(PostControllerImpl.class).apiV1PostsPostIdGet(post.getPostId())).withSelfRel());
		} catch (EntityIsNotExistingException e) {
			logger.error("Not found after Persisting. Should not happen.");
		}
		return post;
	}

	private PostPreviewDTO addSelfLink(PostPreviewDTO post) {
		try {
			post.add(linkTo(methodOn(PostControllerImpl.class).apiV1PostsPostIdGet(post.getPostId())).withSelfRel());
		} catch (EntityIsNotExistingException e) {
			logger.error("Not found after Persisting. Should not happen.");
		}
		return post;
	}

	private Sort getSortingByDateDesc() {
		return new Sort(Direction.DESC, "created");
	}
}
