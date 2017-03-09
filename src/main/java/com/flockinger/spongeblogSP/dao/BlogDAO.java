package com.flockinger.spongeblogSP.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.flockinger.spongeblogSP.model.Blog;

@Repository
public interface BlogDAO extends CrudRepository<Blog,Long>{

}
