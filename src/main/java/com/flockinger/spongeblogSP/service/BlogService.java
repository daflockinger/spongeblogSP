package com.flockinger.spongeblogSP.service;

import com.flockinger.spongeblogSP.dto.BlogDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;

public interface BlogService extends Versionable{
	BlogDTO getBlog() throws EntityIsNotExistingException;
	BlogDTO createBlog(BlogDTO blog) throws DuplicateEntityException;
	void updateBlog(BlogDTO blog) throws EntityIsNotExistingException;
	void deleteBlog() throws EntityIsNotExistingException;
}