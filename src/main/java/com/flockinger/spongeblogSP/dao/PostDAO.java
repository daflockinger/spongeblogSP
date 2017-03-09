package com.flockinger.spongeblogSP.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.Post;

@Repository
public interface PostDAO extends PagingAndSortingRepository<Post,Long>{

}
