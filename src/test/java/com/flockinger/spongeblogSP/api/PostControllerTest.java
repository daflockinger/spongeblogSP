package com.flockinger.spongeblogSP.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.model.enums.PostStatus;
import com.flockinger.spongeblogSP.service.PostService;
import com.google.common.collect.ImmutableList;

public class PostControllerTest extends BaseControllerTest {

  @Autowired
  private PostService service;

  @Autowired
  private PostDAO dao;

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsGet_withNoPaginationSettings_shouldReturnDefaults() throws Exception {
    mockMvc.perform(get("/api/v1/posts").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$.previewPosts", hasSize(5)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("8")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("7")))
        .andExpect(jsonPath("$.previewPosts[2].links[0].href", containsString("1")))
        .andExpect(jsonPath("$.previewPosts[3].links[0].href", containsString("2")))
        .andExpect(jsonPath("$.previewPosts[4].links[0].href", containsString("3")));
  }

  @Test
  @FlywayTest(invokeCleanDB = true)
  public void testApiV1PostsGet_withNoPaginationSettingsAndEmptyDB_shouldReturnEmpty()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsGet_withPage2And2PostsPerPage_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("1")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("2")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsGet_withPage1And3PostPerPage_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts?size=3&page=0").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(3)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("8")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("7")))
        .andExpect(jsonPath("$.previewPosts[2].links[0].href", containsString("1")));
  }

  @Test
  @FlywayTest(invokeCleanDB = true)
  public void testApiV1PostsGet_withEmptyDb_shouldReturnEmpty() throws Exception {
    mockMvc.perform(get("/api/v1/posts?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsPostIdGet_withExistingId_shouldReturnPost() throws Exception {
    mockMvc.perform(get("/api/v1/posts/1").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("somethings")))
        .andExpect(jsonPath("$.content", is("some content...")))
        .andExpect(jsonPath("$.created", not(empty())))
        .andExpect(jsonPath("$.modified", not(empty())))
        .andExpect(jsonPath("$.status", is("PUBLIC")))
        .andExpect(jsonPath("$.author.nickName", is("daflo")))
        .andExpect(jsonPath("$.author.email", is("flo@kinger.cc")))
        .andExpect(jsonPath("$.author.registered", not(empty())))
        .andExpect(jsonPath("$.category.name", is("main category")))
        .andExpect(jsonPath("$.category.rank", is(1)))
        .andExpect(jsonPath("$.tags[0].name", is("cold")))
        .andExpect(jsonPath("$.tags[1].name", is("fancy")));
  }

  @Test
  public void testApiV1PostsPostIdGet_withNotExistngId_shouldReturnNotFound() throws Exception {
    mockMvc.perform(get("/api/v1/posts/34341").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsAuthorUserIdGet_withValidUserAndPaging_shouldCorrect()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/author/1?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("3")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("6")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsAuthorUserIdGet_withValidUser_shouldCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts/author/1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(5)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("1")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("2")))
        .andExpect(jsonPath("$.previewPosts[2].links[0].href", containsString("3")))
        .andExpect(jsonPath("$.previewPosts[3].links[0].href", containsString("6")))
        .andExpect(jsonPath("$.previewPosts[4].links[0].href", containsString("5")));
  }

  @Test
  public void testApiV1PostsAuthorUserIdGet_withNonExistingUser_shouldReturnEmpty()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/author/7657651").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }


  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsAuthorUserIdStatusGet_withValidAndStatusAndPaging_should()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/author/1/PUBLIC?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("3")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("6")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsAuthorUserIdStatusGet_withValidUserAndStatus_should() throws Exception {
    mockMvc.perform(get("/api/v1/posts/author/1/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(5)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("1")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("2")))
        .andExpect(jsonPath("$.previewPosts[2].links[0].href", containsString("3")))
        .andExpect(jsonPath("$.previewPosts[3].links[0].href", containsString("6")))
        .andExpect(jsonPath("$.previewPosts[4].links[0].href", containsString("5")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsAuthorUserIdStatusGet_withInvalidUser_shouldReturnEmpty()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/author/7657651/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsAuthorUserIdStatusGet_withInvalidStatus_shouldReturnConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/author/1/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsAuthorUserIdStatusGet_withInvalidStatusAndInvalidUser_shouldConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/author/7657571/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsPostIdDelete_withValidId_shouldDelete() throws Exception {
    mockMvc.perform(delete("/api/v1/posts/1").contentType(jsonContentType))
        .andExpect(status().isOk());

    assertFalse(dao.exists(1l));
  }

  @Test
  public void testApiV1PostsPostIdDelete_withNotExistingId_shouldReturnNonFound() throws Exception {
    mockMvc.perform(delete("/api/v1/posts/7657651").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1PostsPostIdDelete_withInvalidId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(delete("/api/v1/posts/bla").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
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


    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isCreated());


    PostDTO savedPost = service.getPost(9l);

    assertNotNull(savedPost);
    // verifying author
    assertNotNull(savedPost.getAuthor());
    assertEquals("flo@kinger.cc", savedPost.getAuthor().getEmail());
    assertEquals("daflo", savedPost.getAuthor().getNickName());
    assertNotNull(savedPost.getAuthor().getRegistered());
    // verifying category
    assertNotNull(savedPost.getCategory());
    assertEquals("sub category", savedPost.getCategory().getName());
    assertTrue(savedPost.getCategory().getParentId() == 1l);
    // verifying values
    assertEquals("Some fresh new content...", savedPost.getContent());
    assertNotNull(savedPost.getCreated());
    assertNotNull(savedPost.getModified());
    assertEquals(PostStatus.PUBLIC, savedPost.getStatus());
    assertEquals("Fresh out of the box", savedPost.getTitle());
    // verifying tags
    assertNotNull(savedPost.getTags());
    assertTrue(savedPost.getTags().size() == 1);
    assertEquals("fancy", savedPost.getTags().stream().findFirst().get().getName());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsPost_withNotExistingUser_shouldReturnConflict() throws Exception {
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

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsPost_withNotExistingCategory_shouldReturnConflict() throws Exception {
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


    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsPost_withNotExistingTag_shouldReturnConflict() throws Exception {
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

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
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

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
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

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
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

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
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

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
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

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
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
        mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
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
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsPut_withValidPost_shouldInsert() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = service.getPost(1l);
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));


    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isOk());


    PostDTO savedPost = service.getPost(1l);

    assertNotNull(savedPost);
    // verifying author
    assertNotNull(savedPost.getAuthor());
    assertEquals("flo@kinger.cc", savedPost.getAuthor().getEmail());
    assertEquals("daflo", savedPost.getAuthor().getNickName());
    assertNotNull(savedPost.getAuthor().getRegistered());
    // verifying category
    assertNotNull(savedPost.getCategory());
    assertEquals("sub category", savedPost.getCategory().getName());
    assertTrue(savedPost.getCategory().getParentId() == 1l);
    // verifying values
    assertEquals("Some fresh new content...", savedPost.getContent());
    assertNotNull(savedPost.getCreated());
    assertNotNull(savedPost.getModified());
    assertEquals(PostStatus.PUBLIC, savedPost.getStatus());
    assertEquals("Fresh out of the box", savedPost.getTitle());
    // verifying tags
    assertNotNull(savedPost.getTags());
    assertTrue(savedPost.getTags().size() == 1);
    assertEquals("fancy", savedPost.getTags().stream().findFirst().get().getName());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsPut_withNotExistingUser_shouldReturnConflict() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = service.getPost(1l);
    freshPost.setAuthor(getTestUser(871l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsPut_withNotExistingCategory_shouldReturnConflict() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = service.getPost(1l);
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(8762l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));


    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsPut_withNotExistingTag_shouldReturnConflict() throws Exception {
    Date freshDate = new Date();

    PostDTO freshPost = service.getPost(1l);
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(76551l)));

    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsPut_withNotExistingPost_shouldReturnNotFound() throws Exception {
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

    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
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

    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
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

    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.category", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.content", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.modified", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.status", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.title", containsString("may not be empty")))
        .andExpect(jsonPath("$.fields.tags", containsString("may not be null")));
  }



  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsCategoryCategoryIdGet_withValidCategoryAndPaging_shouldCorrect()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/1?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(1)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("5")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsCategoryCategoryIdGet_withValidCategory_shouldCorrect()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(3)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("1")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("3")))
        .andExpect(jsonPath("$.previewPosts[2].links[0].href", containsString("5")));
  }

  @Test
  public void testApiV1PostsCategoryCategoryIdGet_withNonExistingCategory_shouldReturnEmpty()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/7657651").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }


  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsCategoryCategoryIdStatusGet_withValidAndStatusAndPaging_should()
      throws Exception {
    mockMvc
        .perform(get("/api/v1/posts/category/1/PUBLIC?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(1)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("5")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsCategoryCategoryIdStatusGet_withValidCategoryAndStatus_should()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/1/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(3)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("1")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("3")))
        .andExpect(jsonPath("$.previewPosts[2].links[0].href", containsString("5")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsCategoryCategoryIdStatusGet_withInvalidCategory_shouldReturnEmpty()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/7657651/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsCategoryCategoryIdStatusGet_withInvalidStatus_shouldReturnConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/1/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsCategoryCategoryIdStatusGet_withInvalidStatusAndInvalidCategory_shouldConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/7657571/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }



  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsTagTagIdGet_withValidTagAndPaging_shouldCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/1?size=1&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(1)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("2")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsTagTagIdGet_withValidTag_shouldCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("1")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("2")));
  }

  @Test
  public void testApiV1PostsTagTagIdGet_withNonExistingTag_shouldReturnEmpty() throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/7657651").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }


  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsTagTagIdStatusGet_withValidAndStatusAndPaging_should()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/1/PUBLIC?size=1&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(1)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("2")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsTagTagIdStatusGet_withValidTagAndStatus_should() throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/1/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("1")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("2")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsTagTagIdStatusGet_withInvalidTag_shouldReturnEmpty() throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/7657651/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsTagTagIdStatusGet_withInvalidStatus_shouldReturnConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/1/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsTagTagIdStatusGet_withInvalidStatusAndInvalidTag_shouldConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/7657571/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }



  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsStatusStatusGet_withValidStatusAndPaging_shouldCorrect()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/status/PUBLIC?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("3")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("6")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsStatusStatusGet_withValidStatus_shouldCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts/status/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(5)))
        .andExpect(jsonPath("$.previewPosts[0].links[0].href", containsString("1")))
        .andExpect(jsonPath("$.previewPosts[1].links[0].href", containsString("2")))
        .andExpect(jsonPath("$.previewPosts[2].links[0].href", containsString("3")))
        .andExpect(jsonPath("$.previewPosts[3].links[0].href", containsString("6")))
        .andExpect(jsonPath("$.previewPosts[4].links[0].href", containsString("5")));
  }

  @Test
  public void testApiV1PostsStatusStatusGet_withNonExistingStatus_shouldReturnBadRequest()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/status/nonexista").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsRewindPostIdPut_withExistingPrevVersion_shouldRewind()
      throws Exception {
    PostDTO freshPost = service.getPost(1l);
    freshPost.setCreated(new Date());
    freshPost.setTags(ImmutableList.of(getTag(1l)));
    service.updatePost(freshPost);

    Date freshDate = new Date();
    PostDTO savedPost = service.getPost(1l);
    savedPost.setAuthor(getTestUser(2l));
    savedPost.setCategory(getTestCategory(1l));
    savedPost.setContent("Some updated new content...");
    savedPost.setCreated(freshDate);
    savedPost.setModified(freshDate);
    savedPost.setStatus(PostStatus.MAINTENANCE);
    savedPost.setTitle("Updated out of the box");
    savedPost.setTags(ImmutableList.of(getTag(1l), getTag(3l)));
    service.updatePost(savedPost);

    PostDTO updatedPost = service.getPost(savedPost.getPostId());

    assertNotNull(updatedPost);
    // verifying author
    assertNotNull(updatedPost.getAuthor());
    assertEquals("no@body.cc", updatedPost.getAuthor().getEmail());
    assertEquals("body", updatedPost.getAuthor().getNickName());
    assertNotNull(updatedPost.getAuthor().getRegistered());
    // verifying category
    assertNotNull(updatedPost.getCategory());
    assertEquals("main category", updatedPost.getCategory().getName());
    assertNull(updatedPost.getCategory().getParentId());
    // verifying values
    assertEquals("Some updated new content...", updatedPost.getContent());
    assertNotNull(updatedPost.getCreated());
    assertNotNull(updatedPost.getModified());
    assertEquals(PostStatus.MAINTENANCE, updatedPost.getStatus());
    assertEquals("Updated out of the box", updatedPost.getTitle());
    // verifying tags
    assertNotNull(updatedPost.getTags());
    assertTrue(updatedPost.getTags().size() == 2);
    assertTrue(updatedPost.getTags().stream().anyMatch(tag -> tag.getName().equals("fancy")));
    assertTrue(updatedPost.getTags().stream().anyMatch(tag -> tag.getName().equals("guide")));

    mockMvc
        .perform(put("/api/v1/posts/rewind/" + savedPost.getPostId()).contentType(jsonContentType))
        .andExpect(status().isOk());

    PostDTO rewindPost = service.getPost(savedPost.getPostId());

    assertNotNull(rewindPost);
    // verifying author
    assertNotNull(rewindPost.getAuthor());
    assertEquals("flo@kinger.cc", rewindPost.getAuthor().getEmail());
    assertEquals("daflo", rewindPost.getAuthor().getNickName());
    assertNotNull(rewindPost.getAuthor().getRegistered());
    // verifying category
    assertNotNull(rewindPost.getCategory());
    assertEquals("main category", rewindPost.getCategory().getName());
    assertNull(rewindPost.getCategory().getParentId());
    // verifying values
    assertEquals("some content...", rewindPost.getContent());
    assertNotNull(rewindPost.getCreated());
    assertNotNull(rewindPost.getModified());
    assertEquals(PostStatus.PUBLIC, rewindPost.getStatus());
    assertEquals("somethings", rewindPost.getTitle());
    // verifying tags
    assertNotNull(rewindPost.getTags());
    assertTrue(rewindPost.getTags().size() == 1);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testApiV1PostsRewindPostIdPut_withNoPreviousVersion_shouldReturnConflict()
      throws Exception {
    mockMvc.perform(put("/api/v1/posts/rewind/1").contentType(jsonContentType))
        .andExpect(status().isConflict());
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
