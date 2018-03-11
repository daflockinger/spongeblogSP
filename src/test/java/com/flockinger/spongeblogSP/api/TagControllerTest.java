package com.flockinger.spongeblogSP.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.matches;
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

import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.service.TagService;
import com.google.common.collect.ImmutableList;

public class TagControllerTest extends BaseControllerTest {

  @MockBean
  private TagService service;

  @Test
  public void testapiV1TagsGet_withFullDB_shouldReturnAll() throws Exception {
    TagDTO tag1 = new TagDTO();
    tag1.setName("fancy");
    TagDTO tag2 = new TagDTO();
    tag2.setName("cold");
    TagDTO tag3 = new TagDTO();
    tag3.setName("guide");
    when(service.getAllTags()).thenReturn(ImmutableList.of(tag1, tag2, tag3));
    
    mockMvc.perform(get("/api/v1/tags").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].name", containsString("fancy")))
        .andExpect(jsonPath("$[1].name", containsString("cold")))
        .andExpect(jsonPath("$[2].name", containsString("guide")));
  }

  @Test
  public void testApiV1TagsTagIdGet_withValidId_shouldReturnTag() throws Exception {
    TagDTO tag1 = new TagDTO();
    tag1.setName("fancy");
    when(service.getTag(anyLong())).thenReturn(tag1);
    
    mockMvc.perform(get("/api/v1/tags/1").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("fancy")));
  }

  @Test
  public void testApiV1TagsTagIdGet_withNonExistingId_shouldReturnNotFound() throws Exception {
    when(service.getTag(anyLong())).thenThrow(EntityIsNotExistingException.class);
    
    mockMvc.perform(get("/api/v1/tags/3487634").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }


  @Test
  public void testApiV1TagsTagIdGet_withInvalidId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(get("/api/v1/tags/totalywrong").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1TagsTagIdDelete_withValidId_shouldDelete() throws Exception {
    mockMvc.perform(delete("/api/v1/tags/1").contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(service).deleteTag(idCaptor.capture());
    assertEquals("verify tag with correct ID is deleted", 1l, idCaptor.getValue().longValue());
  }

  @Test
  public void testApiV1TagsTagIdDelete_withNonExistingId_shouldReturnNotFound() throws Exception {
    doThrow(EntityIsNotExistingException.class).when(service).deleteTag(anyLong()); 
    
    mockMvc.perform(delete("/api/v1/tags/9879797").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }


  @Test
  public void testApiV1TagsPost_withValidTag_shouldInsert() throws Exception {
    TagDTO tag = new TagDTO();
    tag.setName("groovy");
    when(service.createTag(anyString())).thenReturn(tag);

    mockMvc.perform(post("/api/v1/tags").content(json(tag)).contentType(jsonContentType))
        .andExpect(status().isCreated());

    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    verify(service).createTag(tagCaptor.capture());
    String createdTagName = tagCaptor.getValue();
    assertEquals("groovy", createdTagName);
  }

  @Test
  public void testApiV1TagsPost_withAlreadyExistingName_shouldReturnConflict() throws Exception {
    when(service.createTag(matches("fancy"))).thenThrow(DuplicateEntityException.class);
    
    TagDTO tag = new TagDTO();
    tag.setName("fancy");

    mockMvc.perform(post("/api/v1/tags").content(json(tag)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1TagsPost_withTagWithEmptyName_shouldReturnBadRequest() throws Exception {
    TagDTO tag = new TagDTO();
    tag.setName("");

    mockMvc.perform(post("/api/v1/tags").content(json(tag)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());

  }

  @Test
  public void testApiV1TagsPost_withTagWithNullName_shouldReturnBadRequest() throws Exception {
    TagDTO tag = new TagDTO();
    tag.setName(null);

    mockMvc.perform(post("/api/v1/tags").content(json(tag)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1TagsPut_withValidTag_shouldUpdate() throws Exception {
    TagDTO tag = new TagDTO();
    tag.setTagId(123l);
    tag.setName("groovy");

    mockMvc.perform(put("/api/v1/tags").content(json(tag)).contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<TagDTO> tagCaptor = ArgumentCaptor.forClass(TagDTO.class);
    verify(service).updateTag(tagCaptor.capture());
    TagDTO createdTag = tagCaptor.getValue();
    assertEquals("groovy", createdTag.getName());
  }

  @Test
  public void testApiV1TagsPut_withAlreadyExistingName_shouldReturnConflict() throws Exception {
    doThrow(DuplicateEntityException.class).when(service).updateTag(any());
    
    TagDTO tag = new TagDTO();
    tag.setTagId(21l);
    tag.setName("fancy");

    mockMvc.perform(put("/api/v1/tags").content(json(tag)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1TagsPut_withNonExistingTag_shouldReturnNotFound() throws Exception {
    doThrow(EntityIsNotExistingException.class).when(service).updateTag(any());
    
    TagDTO tag = new TagDTO();
    tag.setTagId(435354l);
    tag.setName("groovy");

    mockMvc.perform(put("/api/v1/tags").content(json(tag)).contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }


  @Test
  public void testApiV1TagsPut_withTagWithEmptyName_shouldReturnBadRequest() throws Exception {
    TagDTO tag = new TagDTO();
    tag.setTagId(1l);
    tag.setName("");

    mockMvc.perform(post("/api/v1/tags").content(json(tag)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1TagsPut_withTagWithNullName_shouldReturnBadRequest() throws Exception {
    TagDTO tag = new TagDTO();
    tag.setTagId(1l);
    tag.setName(null);

    mockMvc.perform(post("/api/v1/tags").content(json(tag)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1TagsRewindTagIdPut_withValidVersion_shouldRewind() throws Exception {
    Long id = 2l;
    
    mockMvc.perform(put("/api/v1/tags/rewind/" + id).contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(service).rewind(idCaptor.capture());
    assertEquals("verify tag with correct ID is rewound", id.longValue(), idCaptor.getValue().longValue());
  }

  @Test
  public void testApiV1TagsRewindTagIdPut_withNoPrevVersion_shouldReturnConflict()
      throws Exception {
    doThrow(NoVersionFoundException.class).when(service).rewind(anyLong());
    
    mockMvc.perform(put("/api/v1/tags/rewind/1").contentType(jsonContentType))
        .andExpect(status().isConflict());
  }
}
