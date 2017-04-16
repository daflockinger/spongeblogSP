package com.flockinger.spongeblogSP.service.impl;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.dao.BlogDAO;
import com.flockinger.spongeblogSP.dto.BlogDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.Blog;
import com.flockinger.spongeblogSP.service.BlogService;
import com.flockinger.spongeblogSP.service.VersioningService;

@Service
public class BlogServiceImpl implements BlogService{

	@Autowired
	private BlogDAO dao;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private VersioningService<Blog,BlogDAO> versionService;
	
	@Override
	@Transactional(readOnly=true)
	public BlogDTO getBlog() throws EntityIsNotExistingException {
		if(!isAnyBlogExistingAlready()){
			throw new EntityIsNotExistingException("Blog");
		}
		return map(dao.findAll().iterator().next());
	}

	private boolean isAnyBlogExistingAlready(){
		return dao.findAll().iterator().hasNext();
	}
	
	@Override
	@Transactional
	public BlogDTO createBlog(BlogDTO blog) throws DuplicateEntityException {
		if(isAnyBlogExistingAlready()){
			throw new DuplicateEntityException("Blog");
		}
		
		return map(dao.save(map(blog)));
	}
	
	@Override
	@Transactional
	public void updateBlog(BlogDTO blog) throws EntityIsNotExistingException {
		if(!isBlogExisting(blog.getId())){
			throw new EntityIsNotExistingException("Blog");
		}
		dao.save(map(blog));
	}
	
	private boolean isBlogExisting(Long id){
		return dao.findOne(id) != null;
	}

	@Override
	@Transactional
	public void deleteBlog(Long id) throws EntityIsNotExistingException {
		if(!isBlogExisting(id)){
			throw new EntityIsNotExistingException("Blog");
		}
		dao.delete(id);
	}
	
	@Override
	public void rewind(Long id) throws NoVersionFoundException {
		versionService.rewind(id, dao);
	}
	
	private Blog map(BlogDTO tagDto) {
		return mapper.map(tagDto, Blog.class);
	}

	private BlogDTO map(Blog tag) {
		return mapper.map(tag, BlogDTO.class);
	}
}
