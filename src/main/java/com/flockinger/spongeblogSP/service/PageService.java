package com.flockinger.spongeblogSP.service;


import java.util.List;

import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostPreviewDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;

public interface PageService extends Versionable {
  List<PostPreviewDTO> getPages(Boolean withoutCategory);
  
  PostDTO getPage(Long id) throws EntityIsNotExistingException;

  PostDTO createPage(PostDTO post) throws DuplicateEntityException, DependencyNotFoundException;

  void updatePage(PostDTO post)
      throws EntityIsNotExistingException, DuplicateEntityException, DependencyNotFoundException;

  void deletePage(Long id) throws EntityIsNotExistingException;

  void rewind(Long id) throws NoVersionFoundException;
}
