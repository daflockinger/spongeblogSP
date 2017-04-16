package com.flockinger.spongeblogSP.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.flockinger.spongeblogSP.model.BaseModel;

public interface VersionDAO<M extends BaseModel> extends RevisionRepository<M,Long,Integer>, CrudRepository<M, Long>{

}
