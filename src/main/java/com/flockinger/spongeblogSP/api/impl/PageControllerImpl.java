package com.flockinger.spongeblogSP.api.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.PageController;
import com.flockinger.spongeblogSP.api.util.RequestValidator;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostsPage;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.service.PageService;

import io.swagger.annotations.ApiParam;

@RestController
public class PageControllerImpl implements PageController {

  @Autowired
  private PageService service;

  @Autowired
  private RequestValidator validator;

  public ResponseEntity<PostsPage> apiV1PagesGetUsingGET(
      @ApiParam(value = "Filter pages without a category.", defaultValue = "false") @RequestParam(
          value = "without-category", required = false,
          defaultValue = "false") Boolean withoutCategory) {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(service.getPages(withoutCategory));
    return new ResponseEntity<PostsPage>(page, HttpStatus.OK);
  }

  public ResponseEntity<Object> apiV1PagesPutUsingPUT(
      @ApiParam(value = "pageEdit", required = true) @Valid @RequestBody PostDTO pageEdit,
      BindingResult bindingResult) throws DtoValidationFailedException,
      EntityIsNotExistingException, DuplicateEntityException, DependencyNotFoundException {
    validator.validateRequestBody(bindingResult);
    validator.assertIdForUpdate(pageEdit.getPostId());
    service.updatePage(pageEdit);
    return new ResponseEntity<Object>(HttpStatus.OK);
  }

  public ResponseEntity<Object> apiV1PostsPageIdDeleteUsingDELETE(
      @ApiParam(value = "Unique identifier of a Page;",
          required = true) @PathVariable("pageId") Long pageId)
      throws EntityIsNotExistingException {
    service.deletePage(pageId);
    return new ResponseEntity<Object>(HttpStatus.OK);
  }

  public ResponseEntity<PostDTO> apiV1PostsPageIdGetUsingGET(
      @ApiParam(value = "Unique identifier of a Page;",
          required = true) @PathVariable("pageId") Long pageId)
      throws EntityIsNotExistingException {
    PostDTO page = service.getPage(pageId);
    return new ResponseEntity<PostDTO>(page, HttpStatus.OK);
  }

  public ResponseEntity<PostDTO> apiV1PostsPageUsingPOST(
      @ApiParam(value = "pageEdit", required = true) @Valid @RequestBody PostDTO pageEdit,
      BindingResult bindingResult)
      throws DuplicateEntityException, DependencyNotFoundException, DtoValidationFailedException {
    validator.validateRequestBody(bindingResult);
    PostDTO createdPage = service.createPage(pageEdit);
    return new ResponseEntity<PostDTO>(createdPage, HttpStatus.CREATED);
  }
}
