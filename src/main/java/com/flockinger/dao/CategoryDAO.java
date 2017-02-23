package com.flockinger.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.model.Category;

@Repository
public interface CategoryDAO extends PagingAndSortingRepository<Category,Long>{

}
