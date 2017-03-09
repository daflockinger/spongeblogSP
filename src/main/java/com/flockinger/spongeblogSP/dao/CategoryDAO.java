package com.flockinger.spongeblogSP.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.Category;

@Repository
public interface CategoryDAO extends PagingAndSortingRepository<Category,Long>{

}
