package com.flockinger.spongeblogSP.service;

import org.springframework.data.domain.Pageable;

import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostsPage;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.enums.PostStatus;

public interface PostService extends Versionable {
  PostsPage getAllPosts(Pageable pageable);

  PostsPage getAllPostsWithStatus(PostStatus status, Pageable pageable);

  PostsPage getPostsFromCategoryId(Long categoryId, Pageable pageable);

  PostsPage getPostsFromCategoryIdWithStatus(Long categoryId, PostStatus status, Pageable pageable);

  PostsPage getPostsFromAuthorId(Long authorId, Pageable pageable);

  PostsPage getPostsFromAuthorIdWithStatus(Long authorId, PostStatus status, Pageable pageable);

  PostsPage getPostsFromTagId(Long tagId, Pageable pageable);

  PostsPage getPostsFromTagIdWithStatus(Long tagId, PostStatus status, Pageable pageable);

  PostDTO getPost(Long id) throws EntityIsNotExistingException;

  PostDTO createPost(PostDTO post) throws DuplicateEntityException, DependencyNotFoundException;

  void updatePost(PostDTO post)
      throws EntityIsNotExistingException, DuplicateEntityException, DependencyNotFoundException;

  void deletePost(Long id) throws EntityIsNotExistingException;

  void rewind(Long id) throws NoVersionFoundException;
}
