package com.flockinger.spongeblogSP.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostPreviewDTO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.enums.PostStatus;
import com.flockinger.spongeblogSP.service.PageService;
import com.google.common.collect.ImmutableList;

public class PageControllerTest extends BaseControllerTest {
  
  @MockBean
  private PageService service;
  
  @Test
  public void testApiV1PagesGetUsingGET_withCategories_shouldReturnCorrectPages() throws Exception {
    PostPreviewDTO pageWithCategory = new PostPreviewDTO();
    pageWithCategory.setPostId(8l);
    when(service.getPages(anyBoolean())).thenReturn(ImmutableList.of(pageWithCategory));
    
    mockMvc.perform(get("/api/v1/pages").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$.previewPosts", hasSize(1)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(8)));
    
    ArgumentCaptor<Boolean> withCategoryCaptor = ArgumentCaptor.forClass(Boolean.class);
    verify(service).getPages(withCategoryCaptor.capture());
    assertFalse("validate pageService called with without categories flag false", withCategoryCaptor.getValue());
  }

 
  @Test
  public void testApiV1PostsPostIdGet_withExistingId_shouldReturnPost() throws Exception {
    mockMvc.perform(get("/api/v1/pages/1").contentType(jsonContentType)).andExpect(status().isOk());
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(service).getPage(idCaptor.capture());
    assertEquals("verify pageservice getPost with correct id", 1l, idCaptor.getValue().longValue());
  }

  @Test
  public void testApiV1PostsPostIdGet_withNotExistngId_shouldReturnNotFound() throws Exception {
    when(service.getPage(anyLong())).thenThrow(EntityIsNotExistingException.class);
    mockMvc.perform(get("/api/v1/pages/34341").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }


  @Test
  public void testApiV1PostsPostIdDelete_withValidId_shouldDelete() throws Exception {
    mockMvc.perform(delete("/api/v1/pages/1").contentType(jsonContentType))
        .andExpect(status().isOk());
    
    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(service).deletePage(idCaptor.capture());
    assertEquals("verify pageservice deletePost with correct id", 1l, idCaptor.getValue().longValue());
  }

  @Test
  public void testApiV1PostsPostIdDelete_withNotExistingId_shouldReturnNonFound() throws Exception {
    doThrow(EntityIsNotExistingException.class).when(service).deletePage(anyLong());
    
    mockMvc.perform(delete("/api/v1/pages/7657651").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }



  @Test
  public void testApiV1PostsPost_withValidPost_shouldInsert() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));


    mockMvc.perform(post("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isCreated());
    
    verify(service).createPage(any(PostDTO.class));
  }

  @Test
  public void testApiV1PostsPost_withNotExistingUser_shouldReturnConflict() throws Exception {
    when(service.createPage(any(PostDTO.class))).thenThrow(DependencyNotFoundException.class);
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(871l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    mockMvc.perform(post("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1PostsPost_withNotExistingCategory_shouldReturnConflict() throws Exception {
    when(service.createPage(any(PostDTO.class))).thenThrow(DependencyNotFoundException.class);
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(8762l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));


    mockMvc.perform(post("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1PostsPost_withNotExistingTag_shouldReturnConflict() throws Exception {
    when(service.createPage(any(PostDTO.class))).thenThrow(DependencyNotFoundException.class);
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(76551l)));

    mockMvc.perform(post("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }


  @Test
  public void testApiV1PostsPost_withEmptyTitle_shouldReturnBadRequest() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    mockMvc.perform(post("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("Invalid field")))
        .andExpect(jsonPath("$.fields.title", containsString("may not be empty")));
  }

  @Test
  public void testApiV1PostsPost_withNullContent_shouldReturnBadRequest() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent(null);
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    mockMvc.perform(post("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.content", containsString("may not be null")));
  }

  @Test
  public void testApiV1PostsPost_withNullCreated_shouldReturnBadRequest() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(null);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    mockMvc.perform(post("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.created", containsString("may not be null")));
  }

  @Test
  public void testApiV1PostsPost_withNullStatus_shouldReturnBadRequest() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(null);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    mockMvc.perform(post("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.status", containsString("may not be null")));
  }

  @Test
  public void testApiV1PostsPost_withNullCategory_shouldReturnBadRequest() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(null);
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    mockMvc.perform(post("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.category", containsString("may not be null")));
  }


  @Test
  public void testApiV1PostsPost_withAllMessedUpPost_shouldReturnReallyBadRequest()
      throws Exception {
    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(null);
    freshPost.setContent(null);
    freshPost.setCreated(new Date(-7656l));
    freshPost.setModified(null);
    freshPost.setStatus(null);
    freshPost.setTitle("");
    freshPost.setTags(null);

    MvcResult result =
        mockMvc.perform(post("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fields.category", containsString("may not be null")))
            .andExpect(jsonPath("$.fields.content", containsString("may not be null")))
            .andExpect(jsonPath("$.fields.modified", containsString("may not be null")))
            .andExpect(jsonPath("$.fields.status", containsString("may not be null")))
            .andExpect(jsonPath("$.fields.title", containsString("may not be empty")))
            .andExpect(jsonPath("$.fields.tags", containsString("may not be null"))).andReturn();

    System.out.println();
  }


  @Test
  public void testApiV1PostsPut_withValidPost_shouldUpdate() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setPostId(34l);
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    mockMvc.perform(put("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isOk());
    
    verify(service).updatePage(any(PostDTO.class));
  }

  @Test
  public void testApiV1PostsPut_withNotExistingUser_shouldReturnConflict() throws Exception {
    doThrow(DependencyNotFoundException.class).when(service).updatePage(any(PostDTO.class));
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setPostId(34l);
    freshPost.setAuthor(getTestUser(871l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    mockMvc.perform(put("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1PostsPut_withNotExistingCategory_shouldReturnConflict() throws Exception {
    doThrow(DependencyNotFoundException.class).when(service).updatePage(any(PostDTO.class));
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setPostId(34l);
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(8762l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));


    mockMvc.perform(put("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1PostsPut_withNotExistingTag_shouldReturnConflict() throws Exception {
    doThrow(DependencyNotFoundException.class).when(service).updatePage(any(PostDTO.class));
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setPostId(34l);
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(76551l)));

    mockMvc.perform(put("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1PostsPut_withNotExistingPost_shouldReturnNotFound() throws Exception {
    doThrow(EntityIsNotExistingException.class).when(service).updatePage(any(PostDTO.class));
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setPostId(434534l);
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(76551l)));

    mockMvc.perform(put("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1PostsPut_withNullId_shouldReturnBadRequest() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setPostId(null);
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(76551l)));

    mockMvc.perform(put("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1PostsPut_withAllMessedUpPost_shouldReturnReallyBadRequest()
      throws Exception {
    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(null);
    freshPost.setContent(null);
    freshPost.setCreated(new Date(-7656l));
    freshPost.setModified(null);
    freshPost.setStatus(null);
    freshPost.setTitle("");
    freshPost.setTags(null);

    mockMvc.perform(put("/api/v1/pages").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.category", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.content", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.modified", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.status", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.title", containsString("may not be empty")))
        .andExpect(jsonPath("$.fields.tags", containsString("may not be null")));
  }
  
  private TagDTO getTag(Long id) {
    TagDTO tag = new TagDTO();
    tag.setTagId(id);

    return tag;
  }

  private CategoryDTO getTestCategory(Long id) {
    CategoryDTO cat = new CategoryDTO();
    cat.setCategoryId(id);
    return cat;
  }

  private UserInfoDTO getTestUser(Long id) {
    UserInfoDTO user = new UserInfoDTO();
    user.setUserId(id);
    return user;
  }
}
