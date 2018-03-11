package com.flockinger.spongeblogSP.api;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostPreviewDTO;
import com.flockinger.spongeblogSP.dto.PostsPage;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.enums.PostStatus;
import com.flockinger.spongeblogSP.service.PostService;
import com.google.common.collect.ImmutableList;


public class PostControllerTest extends BaseControllerTest {

  @MockBean
  private PostService service;

  @Test
  public void testApiV1PostsGet_withNoPaginationSettings_shouldReturnDefaults() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(8l, 7l, 1l, 2l, 3l));
    when(service.getAllPosts(any())).thenReturn(page);

    mockMvc.perform(get("/api/v1/posts").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$.previewPosts", hasSize(5)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(8)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(7)))
        .andExpect(jsonPath("$.previewPosts[2].postId", is(1)))
        .andExpect(jsonPath("$.previewPosts[3].postId", is(2)))
        .andExpect(jsonPath("$.previewPosts[4].postId", is(3)));
  }

  @Test
  public void testApiV1PostsGet_withNoPaginationSettingsAndEmptyDB_shouldReturnEmpty()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds());
    when(service.getAllPosts(any())).thenReturn(page);

    mockMvc.perform(get("/api/v1/posts").contentType(jsonContentType)).andExpect(status().isOk())
        .andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }

  @Test
  public void testApiV1PostsGet_withPage2And2PostsPerPage_shouldReturnCorrect() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(1l, 2l));
    when(service.getAllPosts(any())).thenReturn(page);

    mockMvc.perform(get("/api/v1/posts?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(1)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(2)));
  }

  @Test
  public void testApiV1PostsGet_withPage1And3PostPerPage_shouldReturnCorrect() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(8l, 7l, 1l));
    when(service.getAllPosts(any())).thenReturn(page);

    mockMvc.perform(get("/api/v1/posts?size=3&page=0").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(3)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(8)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(7)))
        .andExpect(jsonPath("$.previewPosts[2].postId", is(1)));
  }

  @Test
  public void testApiV1PostsPostIdGet_withExistingId_shouldReturnPost() throws Exception {
    when(service.getPost(anyLong())).thenReturn(getFakePost());

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

  private PostDTO getFakePost() {
    PostDTO post = new PostDTO();
    UserInfoDTO author = new UserInfoDTO();
    author.setNickName("daflo");
    author.setEmail("flo@kinger.cc");
    author.setRegistered(new Date());
    post.setAuthor(author);
    CategoryDTO category = new CategoryDTO();
    category.setName("main category");
    category.setRank(1);
    post.setCategory(category);
    post.setContent("some content...");
    post.setCreated(new Date());
    post.setModified(new Date());
    post.setStatus(PostStatus.PUBLIC);
    post.setTitle("somethings");
    TagDTO tag1 = new TagDTO();
    tag1.setName("cold");
    TagDTO tag2 = new TagDTO();
    tag2.setName("fancy");
    post.setTags(ImmutableList.of(tag1, tag2));
    return post;
  }

  @Test
  public void testApiV1PostsPostIdGet_withNotExistngId_shouldReturnNotFound() throws Exception {
    when(service.getPost(anyLong())).thenThrow(EntityIsNotExistingException.class);

    mockMvc.perform(get("/api/v1/posts/34341").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1PostsAuthorUserIdGet_withValidUserAndPaging_shouldCorrect()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(3l, 6l));
    when(service.getPostsFromAuthorId(anyLong(), any())).thenReturn(page);

    mockMvc.perform(get("/api/v1/posts/author/1?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(3)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(6)));
  }

  @Test
  public void testApiV1PostsAuthorUserIdGet_withValidUser_shouldCorrect() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(1l, 2l, 3l, 6l, 5l));
    when(service.getPostsFromAuthorId(anyLong(), any())).thenReturn(page);

    mockMvc.perform(get("/api/v1/posts/author/1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(5)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(1)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(2)))
        .andExpect(jsonPath("$.previewPosts[2].postId", is(3)))
        .andExpect(jsonPath("$.previewPosts[3].postId", is(6)))
        .andExpect(jsonPath("$.previewPosts[4].postId", is(5)));
  }

  @Test
  public void testApiV1PostsAuthorUserIdGet_withNonExistingUser_shouldReturnEmpty()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds());
    when(service.getPostsFromAuthorId(anyLong(), any())).thenReturn(page);

    mockMvc.perform(get("/api/v1/posts/author/7657651").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }


  @Test
  public void testApiV1PostsAuthorUserIdStatusGet_withValidAndStatusAndPaging_should()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(3l, 6l));
    when(service.getPostsFromAuthorIdWithStatus(anyLong(), any(), any())).thenReturn(page);

    mockMvc.perform(get("/api/v1/posts/author/1/PUBLIC?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(3)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(6)));
  }

  @Test
  public void testApiV1PostsAuthorUserIdStatusGet_withValidUserAndStatus_should() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(1l, 2l, 3l, 6l, 5l));
    when(service.getPostsFromAuthorIdWithStatus(anyLong(), any(), any())).thenReturn(page);

    mockMvc.perform(get("/api/v1/posts/author/1/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(5)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(1)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(2)))
        .andExpect(jsonPath("$.previewPosts[2].postId", is(3)))
        .andExpect(jsonPath("$.previewPosts[3].postId", is(6)))
        .andExpect(jsonPath("$.previewPosts[4].postId", is(5)));
  }

  @Test
  public void testApiV1PostsAuthorUserIdStatusGet_withInvalidUser_shouldReturnEmpty()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds());
    when(service.getPostsFromAuthorIdWithStatus(anyLong(), any(), any())).thenReturn(page);

    mockMvc.perform(get("/api/v1/posts/author/7657651/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }

  @Test
  public void testApiV1PostsAuthorUserIdStatusGet_withInvalidStatus_shouldReturnConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/author/1/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1PostsAuthorUserIdStatusGet_withInvalidStatusAndInvalidUser_shouldConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/author/7657571/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1PostsPostIdDelete_withValidId_shouldDelete() throws Exception {
    mockMvc.perform(delete("/api/v1/posts/1").contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(service).deletePost(idCaptor.capture());
    assertEquals("verify post with correct ID is deleted", 1l, idCaptor.getValue().longValue());
  }

  @Test
  public void testApiV1PostsPostIdDelete_withNotExistingId_shouldReturnNonFound() throws Exception {
    doThrow(EntityIsNotExistingException.class).when(service).deletePost(anyLong());

    mockMvc.perform(delete("/api/v1/posts/7657651").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1PostsPostIdDelete_withInvalidId_shouldReturnBadRequest() throws Exception {
    mockMvc.perform(delete("/api/v1/posts/bla").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1PostsPost_withValidPost_shouldInsert() throws Exception {
    when(service.createPost(any())).thenReturn(getFakePostForInsert());

    mockMvc.perform(
        post("/api/v1/posts").content(json(getFakePostForInsert())).contentType(jsonContentType))
        .andExpect(status().isCreated());

    ArgumentCaptor<PostDTO> capturePost = ArgumentCaptor.forClass(PostDTO.class);
    verify(service).createPost(capturePost.capture());
    PostDTO savedPost = capturePost.getValue();

    assertNotNull(savedPost);
    assertEquals("verify correct post author id", 1l,
        savedPost.getAuthor().getUserId().longValue());
    assertEquals("verify correct post category id", 2l,
        savedPost.getCategory().getCategoryId().longValue());
    assertEquals("verify post content", "Some fresh new content...", savedPost.getContent());
    assertNotNull("verify post has created date", savedPost.getCreated());
    assertNotNull("verify post has modified date", savedPost.getModified());
    assertEquals("verify post status", PostStatus.PUBLIC, savedPost.getStatus());
    assertEquals("verify post title", "Fresh out of the box", savedPost.getTitle());
    assertEquals("verify post tag count", 1, savedPost.getTags().size());
    assertEquals("verify post tag id", 1l,
        savedPost.getTags().stream().findFirst().get().getTagId().longValue());
  }

  private PostDTO getFakePostForInsert() {
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
    return freshPost;
  }

  @Test
  public void testApiV1PostsPost_withNotExistingUser_shouldReturnConflict() throws Exception {
    when(service.createPost(any())).thenThrow(DependencyNotFoundException.class);
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setAuthor(getTestUser(871l));

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1PostsPost_withNotExistingCategory_shouldReturnConflict() throws Exception {
    when(service.createPost(any())).thenThrow(DependencyNotFoundException.class);
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setCategory(getTestCategory(8762l));

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1PostsPost_withNotExistingTag_shouldReturnConflict() throws Exception {
    when(service.createPost(any())).thenThrow(DependencyNotFoundException.class);
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setTags(ImmutableList.of(getTag(76551l)));

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }


  @Test
  public void testApiV1PostsPost_withEmptyTitle_shouldReturnBadRequest() throws Exception {
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setTitle("");

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("Invalid field")))
        .andExpect(jsonPath("$.fields.title", containsString("may not be empty")));
  }

  @Test
  public void testApiV1PostsPost_withNullContent_shouldReturnBadRequest() throws Exception {
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setContent(null);

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.content", containsString("may not be null")));
  }

  @Test
  public void testApiV1PostsPost_withNullCreated_shouldReturnBadRequest() throws Exception {
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setCreated(null);

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.created", containsString("may not be null")));
  }

  @Test
  public void testApiV1PostsPost_withNullStatus_shouldReturnBadRequest() throws Exception {
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setStatus(null);

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.status", containsString("may not be null")));
  }

  @Test
  public void testApiV1PostsPost_withNullCategory_shouldReturnBadRequest() throws Exception {
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setCategory(null);

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

    mockMvc.perform(post("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fields.category", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.content", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.modified", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.status", containsString("may not be null")))
        .andExpect(jsonPath("$.fields.title", containsString("may not be empty")))
        .andExpect(jsonPath("$.fields.tags", containsString("may not be null")));
  }


  @Test
  public void testApiV1PostsPut_withValidPost_shouldUpdate() throws Exception {
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setPostId(2l);

    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<PostDTO> postCaptor = ArgumentCaptor.forClass(PostDTO.class);
    verify(service).updatePost(postCaptor.capture());
    PostDTO savedPost = postCaptor.getValue();
    assertNotNull(savedPost);
    assertEquals("verify correct updated user ID", 1l, savedPost.getAuthor().getUserId().longValue());
    assertEquals("verify correct updated category ID", 2l, savedPost.getCategory().getCategoryId().longValue());
    assertEquals("verify correct updated post content", "Some fresh new content...", savedPost.getContent());
    assertNotNull("verify post has created date", savedPost.getCreated());
    assertNotNull("verify post has modified date", savedPost.getModified());
    assertEquals("verify correct updated status", PostStatus.PUBLIC, savedPost.getStatus());
    assertEquals("verify correct updated title", "Fresh out of the box", savedPost.getTitle());
    assertNotNull(savedPost.getTags());
    assertTrue("verify correct tag amount", savedPost.getTags().size() == 1);
    assertEquals("verify correct updated tag ID", 1l, savedPost.getTags().stream().findFirst().get().getTagId().longValue());
  }

  @Test
  public void testApiV1PostsPut_withNotExistingUser_shouldReturnConflict() throws Exception {
    doThrow(DependencyNotFoundException.class).when(service).updatePost(any());
    
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setPostId(2l);
    freshPost.setAuthor(getTestUser(871l));

    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1PostsPut_withNotExistingCategory_shouldReturnConflict() throws Exception {
    doThrow(DependencyNotFoundException.class).when(service).updatePost(any());
    
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setPostId(2l);
    freshPost.setCategory(getTestCategory(8762l));
    
    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1PostsPut_withNotExistingTag_shouldReturnConflict() throws Exception {
    doThrow(DependencyNotFoundException.class).when(service).updatePost(any());
    
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setPostId(2l);
    freshPost.setTags(ImmutableList.of(getTag(76551l)));

    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1PostsPut_withNotExistingPost_shouldReturnNotFound() throws Exception {
    doThrow(EntityIsNotExistingException.class).when(service).updatePost(any());
    
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setPostId(434534l);

    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1PostsPut_withNullId_shouldReturnBadRequest() throws Exception {
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setPostId(null);

    mockMvc.perform(put("/api/v1/posts").content(json(freshPost)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1PostsPut_withAllMessedUpPost_shouldReturnReallyBadRequest()
      throws Exception {
    PostDTO freshPost = getFakePostForInsert();
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
  public void testApiV1PostsCategoryCategoryIdGet_withValidCategoryAndPaging_shouldCorrect()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(5l));
    when(service.getPostsFromCategoryId(anyLong(), any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/category/1?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(1)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(5)));
  }

  @Test
  public void testApiV1PostsCategoryCategoryIdGet_withValidCategory_shouldCorrect()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(1l, 3l, 5l));
    when(service.getPostsFromCategoryId(anyLong(), any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/category/1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(3)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(1)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(3)))
        .andExpect(jsonPath("$.previewPosts[2].postId", is(5)));
  }

  @Test
  public void testApiV1PostsCategoryCategoryIdGet_withNonExistingCategory_shouldReturnEmpty()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds());
    when(service.getPostsFromCategoryId(anyLong(), any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/category/7657651").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }


  @Test
  public void testApiV1PostsCategoryCategoryIdStatusGet_withValidAndStatusAndPaging_should()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(5l));
    when(service.getPostsFromCategoryIdWithStatus(anyLong(), any(), any())).thenReturn(page);
    
    mockMvc
        .perform(get("/api/v1/posts/category/1/PUBLIC?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(1)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(5)));
  }

  @Test
  public void testApiV1PostsCategoryCategoryIdStatusGet_withValidCategoryAndStatus_should()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(1l, 3l, 5l));
    when(service.getPostsFromCategoryIdWithStatus(anyLong(), any(), any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/category/1/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(3)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(1)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(3)))
        .andExpect(jsonPath("$.previewPosts[2].postId", is(5)));
  }

  @Test
  public void testApiV1PostsCategoryCategoryIdStatusGet_withInvalidCategory_shouldReturnEmpty()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds());
    when(service.getPostsFromCategoryIdWithStatus(anyLong(), any(), any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/category/7657651/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }

  @Test
  public void testApiV1PostsCategoryCategoryIdStatusGet_withInvalidStatus_shouldReturnConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/1/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1PostsCategoryCategoryIdStatusGet_withInvalidStatusAndInvalidCategory_shouldConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/7657571/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1PostsTagTagIdGet_withValidTagAndPaging_shouldCorrect() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(2l));
    when(service.getPostsFromTagId(anyLong(), any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/tag/1?size=1&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(1)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(2)));
  }

  @Test
  public void testApiV1PostsTagTagIdGet_withValidTag_shouldCorrect() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(1l,2l));
    when(service.getPostsFromTagId(anyLong(), any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/tag/1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(1)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(2)));
  }

  @Test
  public void testApiV1PostsTagTagIdGet_withNonExistingTag_shouldReturnEmpty() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds());
    when(service.getPostsFromTagId(anyLong(), any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/tag/7657651").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }


  @Test
  public void testApiV1PostsTagTagIdStatusGet_withValidAndStatusAndPaging_should()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(2l));
    when(service.getPostsFromTagIdWithStatus(anyLong(),any(),any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/tag/1/PUBLIC?size=1&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(1)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(2)));
  }

  @Test
  public void testApiV1PostsTagTagIdStatusGet_withValidTagAndStatus_should() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(1l, 2l));
    when(service.getPostsFromTagIdWithStatus(anyLong(),any(),any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/tag/1/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(1)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(2)));
  }

  @Test
  public void testApiV1PostsTagTagIdStatusGet_withInvalidTag_shouldReturnEmpty() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds());
    when(service.getPostsFromTagIdWithStatus(anyLong(),any(),any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/tag/7657651/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(0)));
  }

  @Test
  public void testApiV1PostsTagTagIdStatusGet_withInvalidStatus_shouldReturnConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/1/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1PostsTagTagIdStatusGet_withInvalidStatusAndInvalidTag_shouldConflict()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/7657571/BLA").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }



  @Test
  public void testApiV1PostsStatusStatusGet_withValidStatusAndPaging_shouldCorrect()
      throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(3l,6l));
    when(service.getAllPostsWithStatus(any(),any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/status/PUBLIC?size=2&page=1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(2)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(3)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(6)));
  }

  @Test
  public void testApiV1PostsStatusStatusGet_withValidStatus_shouldCorrect() throws Exception {
    PostsPage page = new PostsPage();
    page.setPreviewPosts(createPreviewsWithIds(1l, 2l, 3l, 6l, 5l));
    when(service.getAllPostsWithStatus(any(),any())).thenReturn(page);
    
    mockMvc.perform(get("/api/v1/posts/status/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.previewPosts", hasSize(5)))
        .andExpect(jsonPath("$.previewPosts[0].postId", is(1)))
        .andExpect(jsonPath("$.previewPosts[1].postId", is(2)))
        .andExpect(jsonPath("$.previewPosts[2].postId", is(3)))
        .andExpect(jsonPath("$.previewPosts[3].postId", is(6)))
        .andExpect(jsonPath("$.previewPosts[4].postId", is(5)));
  }

  @Test
  public void testApiV1PostsStatusStatusGet_withNonExistingStatus_shouldReturnBadRequest()
      throws Exception {
    mockMvc.perform(get("/api/v1/posts/status/nonexista").contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1PostsRewindPostIdPut_withExistingPrevVersion_shouldRewind()
      throws Exception {
    PostDTO freshPost = getFakePostForInsert();
    freshPost.setPostId(2l);

    mockMvc
        .perform(put("/api/v1/posts/rewind/" + freshPost.getPostId()).contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(service).rewind(idCaptor.capture());
    assertEquals("verify post with correct ID was rewound", 2l, idCaptor.getValue().longValue());
  }

  @Test
  public void testApiV1PostsRewindPostIdPut_withNoPreviousVersion_shouldReturnConflict()
      throws Exception {
    doThrow(NoVersionFoundException.class).when(service).rewind(anyLong());
    
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

  private List<PostPreviewDTO> createPreviewsWithIds(Long... ids) {
    return Arrays.asList(ids).stream().map(this::createPreviewWithId).collect(Collectors.toList());
  }

  private PostPreviewDTO createPreviewWithId(Long id) {
    PostPreviewDTO preview = new PostPreviewDTO();
    preview.setPostId(id);
    return preview;
  }
}
