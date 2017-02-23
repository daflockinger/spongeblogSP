package com.flockinger.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.model.Tag;

@Repository
public interface TagDAO extends PagingAndSortingRepository<Tag,Long>{

}
