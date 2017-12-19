package com.flockinger.spongeblogSP.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostPreviewDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.service.PageService;
import com.flockinger.spongeblogSP.service.PostService;

@Service
public class PageServiceImpl implements PageService {

  @Autowired
  private PostService postService;
  @Autowired
  private PostDAO dao;
  @Autowired
  private ModelMapper mapper;
  
  @Override
  public List<PostPreviewDTO> getPages(Boolean withoutCategory) {
    List<Post> pages = new ArrayList<>();
    if(withoutCategory) {
      pages = dao.findPageWithoutCategory();
    } else {
      pages = dao.findPageWithCategory();
    }
    return pages.stream().map(this::mapPreview).collect(Collectors.toList());
  }
  
  public PostPreviewDTO mapPreview(Post post) {
    PostPreviewDTO preview = mapper.map(post, PostPreviewDTO.class);
    preview.setPartContent("");
    return preview;
  }

  @Override
  public PostDTO getPage(Long id) throws EntityIsNotExistingException {
    return postService.getPost(id);
  }

  @Override
  public PostDTO createPage(PostDTO post)
      throws DuplicateEntityException, DependencyNotFoundException {
    return postService.createPost(post);
  }

  @Override
  public void updatePage(PostDTO post)
      throws EntityIsNotExistingException, DuplicateEntityException, DependencyNotFoundException {
    postService.updatePost(post);
  }

  @Override
  public void deletePage(Long id) throws EntityIsNotExistingException {
    postService.deletePost(id);
  }

  @Override
  public void rewind(Long id) throws NoVersionFoundException {
    postService.rewind(id);
  }
}
