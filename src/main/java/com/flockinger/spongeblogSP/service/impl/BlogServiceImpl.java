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
public class BlogServiceImpl implements BlogService {

  @Autowired
  private BlogDAO dao;

  @Autowired
  private ModelMapper mapper;

  @Autowired
  private VersioningService<Blog, BlogDAO> versionService;

  @Override
  @Transactional(readOnly = true)
  public BlogDTO getBlog() throws EntityIsNotExistingException {
    if (!isAnyBlogExistingAlready()) {
      throw new EntityIsNotExistingException("Blog");
    }
    return map(fetchBlog());
  }

  private boolean isAnyBlogExistingAlready() {
    return dao.findAll().iterator().hasNext();
  }

  private Blog fetchBlog() {
    return dao.findAll().iterator().next();
  }

  @Override
  @Transactional
  public BlogDTO createBlog(BlogDTO blog) throws DuplicateEntityException {
    if (isAnyBlogExistingAlready()) {
      throw new DuplicateEntityException("Blog");
    }

    return map(dao.save(map(blog)));
  }

  @Override
  @Transactional
  public void updateBlog(BlogDTO blog) {
    Blog toUpdateBlog = map(blog);
    toUpdateBlog.setId(fetchBlog().getId());

    dao.save(toUpdateBlog);
  }

  @Override
  @Transactional
  public void deleteBlog() throws EntityIsNotExistingException {
    if (!isAnyBlogExistingAlready()) {
      throw new EntityIsNotExistingException("Blog");
    }
    dao.delete(fetchBlog().getId());
  }

  @Override
  public void rewind(Long id) throws NoVersionFoundException {
    if (isAnyBlogExistingAlready()) {
      versionService.rewind(fetchBlog().getId(), dao);
    }
  }

  private Blog map(BlogDTO tagDto) {
    return mapper.map(tagDto, Blog.class);
  }

  private BlogDTO map(Blog tag) {
    return mapper.map(tag, BlogDTO.class);
  }
}
