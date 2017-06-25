package com.flockinger.spongeblogSP.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.model.enums.PostStatus;

@Repository
public interface PostDAO extends PagingAndSortingRepository<Post,Long>, VersionDAO<Post>{
	Post findByTitle(String title);
	
	List<Post> findByTagsId(Long id);
	
	Page<Post> findByTagsId(Long id, Pageable pageable);
	
	Page<Post> findByTagsIdAndStatus(Long id, PostStatus status, Pageable pageable);
	
	List<Post> findByAuthorId(Long id);
	
	Page<Post> findByStatus (PostStatus status, Pageable pageable);
	
	Page<Post> findByCategoryId (Long categoryId, Pageable pageable);
	
	Page<Post> findByCategoryIdAndStatus (Long categoryId, PostStatus status, Pageable pageable);
	
	Page<Post> findByAuthorId (Long authorId, Pageable pageable);
	
	Page<Post> findByAuthorIdAndStatus (Long authorId, PostStatus status, Pageable pageable);
}
