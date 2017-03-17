package com.flockinger.spongeblogSP.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.model.Category;

@Component
public class TestService{
	
	@Autowired
	private CategoryDAO dao;
	
	@Transactional(readOnly=true)
	public Category findOne(Long id){
		Category cat = dao.findOne(id);
		cat.getSubCategories().size();
		return cat;
	}
	
	@Transactional
	public Category save(Category category){
		return dao.save(category);
	}
	
	@Transactional
	public void update(Category category){
		dao.update(category);
	}
	
	@Transactional
	public void delete(Long id){
		dao.delete(id);
	}
}
