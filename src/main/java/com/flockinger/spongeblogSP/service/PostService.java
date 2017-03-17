package com.flockinger.spongeblogSP.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.link.PostLink;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.model.enums.PostStatus;

public interface PostService {
	List<PostLink> getAllPosts(Pageable pageable);
	
	List<PostLink> getAllPostsWithStatus(PostStatus status, Pageable pageable);
	
	List<PostLink> getPostsFromCategoryId(Long categoryId, Pageable pageable);
	
	List<PostLink> getPostsFromCategoryIdWithStatus(Long categoryId, PostStatus status, Pageable pageable);
	
	List<PostLink> getPostsFromAuthorId(Long authorId, Pageable pageable);
	
	List<PostLink> getPostsFromAuthorIdWithStatus(Long authorId, PostStatus status, Pageable pageable);
	
	PostDTO getPost(Long id) throws EntityIsNotExistingException;
	
	PostDTO createPost(PostDTO post) throws DuplicateEntityException;
	
	void updatePost(PostDTO post) throws EntityIsNotExistingException, DuplicateEntityException;
	
	void deletePost(PostDTO post) throws EntityIsNotExistingException;
}
