package com.flockinger.spongeblogSP.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.model.enums.PostStatus;

@Repository
public interface PostDAO extends PagingAndSortingRepository<Post,Long>, VersionDAO<Post>{
	Post findByTitle(String title);
	
	List<Post> findByTagsId(Long id);
	
	List<Post> findByTagsId(Long id, Pageable pageable);
	
	List<Post> findByTagsIdAndStatus(Long id, PostStatus status, Pageable pageable);
	
	List<Post> findByAuthorId(Long id);
	
	List<Post> findByStatus (PostStatus status, Pageable pageable);
	
	List<Post> findByCategoryId (Long categoryId, Pageable pageable);
	
	List<Post> findByCategoryIdAndStatus (Long categoryId, PostStatus status, Pageable pageable);
	
	List<Post> findByAuthorId (Long authorId, Pageable pageable);
	
	List<Post> findByAuthorIdAndStatus (Long authorId, PostStatus status, Pageable pageable);
}
