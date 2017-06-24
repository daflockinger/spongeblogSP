package com.flockinger.spongeblogSP.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostPreviewDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.enums.PostStatus;

public interface PostService extends Versionable{
	List<PostPreviewDTO> getAllPosts(Pageable pageable);
	
	List<PostPreviewDTO> getAllPostsWithStatus(PostStatus status, Pageable pageable);
	
	List<PostPreviewDTO> getPostsFromCategoryId(Long categoryId, Pageable pageable);
	
	List<PostPreviewDTO> getPostsFromCategoryIdWithStatus(Long categoryId, PostStatus status, Pageable pageable);
	
	List<PostPreviewDTO> getPostsFromAuthorId(Long authorId, Pageable pageable);

	List<PostPreviewDTO> getPostsFromAuthorIdWithStatus(Long authorId, PostStatus status, Pageable pageable);
	
	List<PostPreviewDTO> getPostsFromTagId(Long tagId, Pageable pageable);
	
	List<PostPreviewDTO> getPostsFromTagIdWithStatus(Long tagId, PostStatus status, Pageable pageable);

	PostDTO getPost(Long id) throws EntityIsNotExistingException;

	PostDTO createPost(PostDTO post) throws DuplicateEntityException, DependencyNotFoundException;
	
	void updatePost(PostDTO post) throws EntityIsNotExistingException, DuplicateEntityException, DependencyNotFoundException;
	
	void deletePost(Long id) throws EntityIsNotExistingException;
	
	void rewind(Long id) throws NoVersionFoundException;
}
