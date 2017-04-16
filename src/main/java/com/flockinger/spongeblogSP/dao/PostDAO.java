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
	
	List<Post> findByAuthorId(Long id);
	
	@Query("select distinct id from Post")
	List<Long> findAllIdsDistinct (Pageable pageable);
	
	@Query("select distinct id from Post p where p.status=?1")
	List<Long> findIdDistinctByStatus (PostStatus status, Pageable pageable);
	
	@Query("select distinct id from Post p where p.category.id=?1")
	List<Long> findDistinctIdByCategoryId (Long categoryId, Pageable pageable);
	
	@Query("select distinct id from Post p where p.category.id=?1 and p.status=?2")
	List<Long> findDistinctIdByCategoryIdAndStatus (Long categoryId, PostStatus status, Pageable pageable);
	
	@Query("select distinct id from Post p where p.author.id=?1")
	List<Long> findDistinctIdByAuthorId (Long authorId, Pageable pageable);
	
	@Query("select distinct id from Post p where p.author.id=?1 and p.status=?2")
	List<Long> findDistinctIdByAuthorIdAndStatus (Long authorId, PostStatus status, Pageable pageable);
}
