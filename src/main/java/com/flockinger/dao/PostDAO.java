package com.flockinger.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.model.Post;

@Repository
public interface PostDAO extends PagingAndSortingRepository<Post,Long>{

}
