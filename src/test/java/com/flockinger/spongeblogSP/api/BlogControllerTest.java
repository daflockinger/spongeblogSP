package com.flockinger.spongeblogSP.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
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

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.flockinger.spongeblogSP.dto.BlogDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.enums.BlogStatus;
import com.flockinger.spongeblogSP.service.BlogService;
import com.google.common.collect.ImmutableMap;



public class BlogControllerTest extends BaseControllerTest {

  @MockBean
  private BlogService service;

  
  @Test
  public void testApiV1BlogGet_withExistingBlog_shouldReturnCorrect() throws Exception {
    BlogDTO blog = new BlogDTO();
    blog.setName("test blog");
    blog.setStatus(BlogStatus.ACTIVE);
    blog.setSettings(ImmutableMap.of("blog theme","all-black","footer","hide"));
    
    when(service.getBlog()).thenReturn(blog);
    
    mockMvc.perform(get("/api/v1/blog").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("test blog")))
        .andExpect(jsonPath("$.status", is(BlogStatus.ACTIVE.toString())))
        .andExpect(jsonPath("$.settings['blog theme']", is("all-black")))
        .andExpect(jsonPath("$.settings.footer", is("hide")));
  }

  @Test
  public void testApiV1BlogGet_withNoneExistingBlog_shouldReturnNotFound() throws Exception {
    when(service.getBlog()).thenThrow(EntityIsNotExistingException.class);
    
    mockMvc.perform(get("/api/v1/blog").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1BlogPost_withValidBlog_shouldWork() throws Exception {
    BlogDTO newBlog = new BlogDTO();
    newBlog.setName("Spacy blog");
    newBlog.setStatus(BlogStatus.DISABLED);
    newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));
    
    when(service.createBlog(any())).thenReturn(newBlog);

    mockMvc.perform(post("/api/v1/blog").content(json(newBlog)).contentType(jsonContentType))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.name", is("Spacy blog")))
        .andExpect(jsonPath("$.status", is(BlogStatus.DISABLED.toString())))
        .andExpect(jsonPath("$.settings['text-color']", is("white")))
        .andExpect(jsonPath("$.settings.['background-image']", is("bunny.jpg")));

    verify(service).createBlog(any());
  }

  @Test
  public void testApiV1BlogPost_withInvalidName_shouldReturnBadRequest() throws Exception {
    BlogDTO newBlog = new BlogDTO();
    newBlog.setName("");
    newBlog.setStatus(BlogStatus.DISABLED);
    newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));

    mockMvc.perform(post("/api/v1/blog").content(json(newBlog)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1BlogPost_withNullName_shouldReturnBadRequest() throws Exception {
    BlogDTO newBlog = new BlogDTO();
    newBlog.setName(null);
    newBlog.setStatus(BlogStatus.DISABLED);
    newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));

    mockMvc.perform(post("/api/v1/blog").content(json(newBlog)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1BlogPost_withNullStatus_shouldReturnBadRequest() throws Exception {
    BlogDTO newBlog = new BlogDTO();
    newBlog.setName("non status blog");
    newBlog.setStatus(null);
    newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));

    mockMvc.perform(post("/api/v1/blog").content(json(newBlog)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1BlogPost_withNullSettings_shouldReturnBadRequest() throws Exception {
    BlogDTO newBlog = new BlogDTO();
    newBlog.setName("non status blog");
    newBlog.setStatus(null);
    newBlog.setSettings(null);

    mockMvc.perform(post("/api/v1/blog").content(json(newBlog)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1BlogPost_withAlreadyExistingBlog_shouldRetunConflict() throws Exception {
    when(service.createBlog(any())).thenThrow(DuplicateEntityException.class);
    
    BlogDTO newBlog = new BlogDTO();
    newBlog.setName("Spacy blog");
    newBlog.setStatus(BlogStatus.DISABLED);
    newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));

    mockMvc.perform(post("/api/v1/blog").content(json(newBlog)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1BlogPut_withValidUpdate_shouldUpdate() throws Exception {
    BlogDTO savedBlog = new BlogDTO();
    savedBlog.setName("Hyper-Space blog");
    savedBlog.setStatus(BlogStatus.ACTIVE);
    savedBlog
        .setSettings(ImmutableMap.of("text-color", "black", "background-image", "enterprise.jpg"));

    mockMvc.perform(put("/api/v1/blog").content(json(savedBlog)).contentType(jsonContentType))
        .andExpect(status().isOk());

    
    ArgumentCaptor<BlogDTO> blogCaptor = ArgumentCaptor.forClass(BlogDTO.class);
    verify(service).updateBlog(blogCaptor.capture());
    
    BlogDTO updatedBlog = blogCaptor.getValue();
    assertEquals("Hyper-Space blog", updatedBlog.getName());
    assertEquals(BlogStatus.ACTIVE, updatedBlog.getStatus());
    assertThat(savedBlog.getSettings()).containsAllEntriesOf(
        ImmutableMap.of("text-color", "black", "background-image", "enterprise.jpg"));
  }

  @Test
  public void testApiV1BlogPut_withInvalidName_shouldReturnBadRequest() throws Exception {
    BlogDTO savedBlog = new BlogDTO();

    savedBlog.setName("");
    savedBlog.setStatus(BlogStatus.ACTIVE);
    savedBlog
        .setSettings(ImmutableMap.of("text-color", "black", "background-image", "enterprise.jpg"));

    mockMvc.perform(put("/api/v1/blog").content(json(savedBlog)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1BlogPut_withInvalidStatus_shouldReturnBadRequest() throws Exception {
    BlogDTO savedBlog = new BlogDTO();

    savedBlog.setName("Space");
    savedBlog.setStatus(null);
    savedBlog
        .setSettings(ImmutableMap.of("text-color", "black", "background-image", "enterprise.jpg"));

    mockMvc.perform(put("/api/v1/blog").content(json(savedBlog)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1BlogPut_withInvalidSettings_shouldReturnBadRequest() throws Exception {
    BlogDTO savedBlog = new BlogDTO();

    savedBlog.setName("Space");
    savedBlog.setStatus(BlogStatus.ACTIVE);
    savedBlog.setSettings(null);

    mockMvc.perform(put("/api/v1/blog").content(json(savedBlog)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1BlogDelete_withExistingBlog_shouldDelete() throws Exception {
    mockMvc.perform(delete("/api/v1/blog").contentType(jsonContentType)).andExpect(status().isOk());
    
    verify(service).deleteBlog();
  }

  @Test
  public void testApiV1BlogDelete_withNonExistingBlog_shouldReturnNotFound() throws Exception {
    doThrow(EntityIsNotExistingException.class).when(service).deleteBlog();
    
    mockMvc.perform(delete("/api/v1/blog").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1BlogRewindPut_withExistingVersions_shouldRewindToOldBlog() throws Exception {

    mockMvc.perform(put("/api/v1/blog/rewind").contentType(jsonContentType))
        .andExpect(status().isOk());

    verify(service).rewind(anyLong());
  }


  @Test
  public void testApiV1BlogRewindPut_withNoVersions_shouldReturnConflict() throws Exception {
    doThrow(NoVersionFoundException.class).when(service).rewind(anyLong());
    
    mockMvc.perform(put("/api/v1/blog/rewind").contentType(jsonContentType))
        .andExpect(status().isConflict());
  }
}
