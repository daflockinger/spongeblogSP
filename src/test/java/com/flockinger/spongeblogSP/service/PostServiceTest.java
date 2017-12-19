package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostPreviewDTO;
import com.flockinger.spongeblogSP.dto.PostsPage;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.enums.PostStatus;
import com.google.common.collect.ImmutableList;

public class PostServiceTest extends BaseServiceTest {

  @Autowired
  private PostService service;

  @Autowired
  private PostDAO dao;

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllPosts_withPaging_shouldReturnPageContent() {
    Pageable secondPageDateDescending = new PageRequest(1, 3, createSort(Direction.DESC, "created"));

    PostsPage page = service.getAllPosts(secondPageDateDescending);
    List<PostPreviewDTO> posts = page.getPreviewPosts();

    assertNotNull("validate post list not null", posts);
    assertEquals("validate post list first sorted entry id", 2l, posts.get(0).getPostId().longValue());
    assertEquals("validate post list second entry id", 3l, posts.get(1).getPostId().longValue());
    assertEquals("validate post list third entry id", 6l, posts.get(2).getPostId().longValue());
    assertTrue("validate posts page has previous page", page.getHasPrevious());
    assertTrue("validate posts page has next page", page.getHasNext());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllPostWithStatus_withValidStatus_shouldReturnFiltered() {
    Pageable firstPageDateDescending = new PageRequest(0, 6, createSort(Direction.DESC, "created"));

    PostsPage page = service.getAllPostsWithStatus(PostStatus.PUBLIC, firstPageDateDescending);
    List<PostPreviewDTO> posts = page.getPreviewPosts();
    assertNotNull("validate post list not null",posts);
    assertEquals("validate first post status list entry id", 1l, posts.get(0).getPostId().longValue());
    assertEquals("validate second post status list entry id", 2l, posts.get(1).getPostId().longValue());
    assertEquals("validate second entries user email", "flo@kinger.cc", posts.get(1).getAuthor().getEmail());
    assertEquals("validate second entries user nickname", "daflo", posts.get(1).getAuthor().getNickName());
    assertNotNull("validate second entries has registered date", posts.get(1).getAuthor().getRegistered());
    assertEquals("validate second entries category name", "sub category", posts.get(1).getCategory().getName());
    assertEquals("validate second entries category id", 2l, posts.get(1).getCategory().getCategoryId().longValue());
    assertNotNull("validate second entries has created date", posts.get(1).getCreated());
    assertNotNull("validate second entries has modified date", posts.get(1).getModified());
    assertEquals("validate second entries part content", "anot...", posts.get(1).getPartContent());
    assertEquals("validate second entries status", PostStatus.PUBLIC, posts.get(1).getStatus());
    assertEquals("validate second entries tag count", 1, posts.get(1).getTags().size());
    assertTrue("validate second entries tags", posts.get(1).getTags().stream().anyMatch(tag -> tag.getName().equals("fancy")));
    assertEquals("validate second entries ", "always", posts.get(1).getTitle());
    assertEquals("validate third post entry id ", 3l, posts.get(2).getPostId().longValue());
    assertEquals("validate fourth post entry id",6l, posts.get(3).getPostId().longValue());
    assertEquals("validate fifth post entry id", 5l, posts.get(4).getPostId().longValue());
    assertFalse("validate posts status page has no next page", page.getHasNext());
    assertFalse("validate posts status page has no previous page", page.getHasPrevious());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllPostFromCategoryId_withValidCategory_shouldReturnFiltered() {
    Pageable firstPageDateDescending = new PageRequest(0, 6, createSort(Direction.DESC, "created"));

    PostsPage page = service.getPostsFromCategoryId(1l, firstPageDateDescending);
    List<PostPreviewDTO> posts = page.getPreviewPosts();
    assertNotNull("validate post list not null",posts);
    assertEquals("validate first post by category entry id", 1l, posts.get(0).getPostId().longValue());
    assertEquals("validate second post by category entry id", 3l, posts.get(1).getPostId().longValue());
    assertEquals("validate third post by category entry id", 5l, posts.get(2).getPostId().longValue());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllPostFromCategoryIdAndStatus_withValidCategoryAndStatus_shouldReturnFiltered() {
    Pageable firstPageDateDescending = new PageRequest(0, 6, createSort(Direction.DESC, "created"));

    PostsPage page =
        service.getPostsFromCategoryIdWithStatus(2l, PostStatus.PUBLIC, firstPageDateDescending);
    List<PostPreviewDTO> posts = page.getPreviewPosts();
    assertNotNull("validate post list not null",posts);
    assertEquals("validate first post by category and status entry id", 2l, posts.get(0).getPostId().longValue());
    assertEquals("validate second post by category and status entry id", 6l, posts.get(1).getPostId().longValue());
  }


  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllPostFromTagId_withValidTag_shouldReturnFiltered() {
    Pageable firstPageDateDescending = new PageRequest(0, 6, createSort(Direction.DESC, "created"));

    PostsPage page = service.getPostsFromTagId(1l, firstPageDateDescending);
    List<PostPreviewDTO> posts = page.getPreviewPosts();
    assertNotNull("validate post list not null",posts);
    assertEquals("validate first post by tag entry id", 1l, posts.get(0).getPostId().longValue());
    assertEquals("validate second post by tag entry id", 2l, posts.get(1).getPostId().longValue());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllPostFromTagIdAndStatus_withValidTagAndStatus_shouldReturnFiltered() {
    Pageable firstPageDateDescending = new PageRequest(0, 6, createSort(Direction.DESC, "created"));

    PostsPage page =
        service.getPostsFromTagIdWithStatus(1l, PostStatus.PUBLIC, firstPageDateDescending);
    List<PostPreviewDTO> posts = page.getPreviewPosts();
    assertNotNull("validate post list not null",posts);
    assertEquals("validate first post by tag and status entry id", 1l, posts.get(0).getPostId().longValue());
    assertEquals("validate second post by tag and status entry id", 2l, posts.get(1).getPostId().longValue());
  }


  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllPostFromAuthor_withValidAuthor_shouldReturnFiltered() {
    Pageable firstPageDateDescending = new PageRequest(0, 6, createSort(Direction.DESC, "created"));

    PostsPage page = service.getPostsFromAuthorId(1l, firstPageDateDescending);
    List<PostPreviewDTO> posts = page.getPreviewPosts();
    assertNotNull("validate returned posts list is not null", posts);
    assertEquals("validate first post by user entry id", 1l, posts.get(0).getPostId().longValue());
    assertEquals("validate second post by user entry id", 2l, posts.get(1).getPostId().longValue());
    assertEquals("validate third post by user entry id", 3l, posts.get(2).getPostId().longValue());
    assertEquals("validate fourth post by user entry id", 6l, posts.get(3).getPostId().longValue());
    assertEquals("validate fifth post by user entry id", 5l, posts.get(4).getPostId().longValue());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllPostFromAuthorAndStatus_withValidAuthorAndStatus_shouldReturnFiltered() {
    Pageable firstPageDateDescending = new PageRequest(0, 6, createSort(Direction.DESC, "created"));

    PostsPage page =
        service.getPostsFromAuthorIdWithStatus(1l, PostStatus.DELETED, firstPageDateDescending);
    List<PostPreviewDTO> posts = page.getPreviewPosts();
    assertNotNull("validate returned posts list is not null", posts);
    assertEquals("validate first post by user and status entry id", 4l, posts.get(0).getPostId().longValue());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetPost_withValidId_shouldReturnPost() throws EntityIsNotExistingException {
    PostDTO post = service.getPost(1l);

    assertNotNull("validate read post not null", post);
    assertEquals("validate correct post content", "some content...", post.getContent());
    assertNotNull("validate correct post created date exists",post.getCreated());
    assertNotNull("validate correct post modified date exists",post.getModified());
    assertEquals("validate correct post status", PostStatus.PUBLIC, post.getStatus());
    assertEquals("validate correct post title", "somethings", post.getTitle());

    UserInfoDTO user = post.getAuthor();
    assertNotNull("validate post author not null", user);
    assertEquals("validate correct post author email", "flo@kinger.cc", user.getEmail());
    assertEquals("validate correct post author id", 1l, user.getUserId().longValue());
    assertEquals("validate correct post author nickname", "daflo", user.getNickName());
    assertNotNull("validate correct post author has registered date", user.getRegistered());

    CategoryDTO category = post.getCategory();
    assertNotNull("validate post category not null", category);
    assertEquals("validate correct post category name", "main category", category.getName());
    assertEquals("validate correct post category rank", 1l, category.getRank().longValue());

    List<TagDTO> tags = post.getTags();
    assertNotNull("validate post tags not null", tags);
    assertEquals("validate correct post tags count", 2, tags.size());
    assertTrue("validate first tag is correct", tags.stream().anyMatch(tag -> tag.getName().equals("fancy")));
    assertTrue("validate second tag is correct", tags.stream().anyMatch(tag -> tag.getName().equals("cold")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreatePost_withValidPost_shouldCreateAndDeleteThenPost()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
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

    // create new Post
    Long freshId = service.createPost(freshPost).getPostId();

    PostDTO savedPost = service.getPost(freshId);

    assertNotNull("validate returned saved post not null", savedPost);
    // verifying author
    assertNotNull("validate saved post author not null", savedPost.getAuthor());
    assertEquals("validate correct saved post author email", "flo@kinger.cc", savedPost.getAuthor().getEmail());
    assertEquals("validate correct saved post author nickname", "daflo", savedPost.getAuthor().getNickName());
    assertNotNull("validate correct saved post author has registered date", savedPost.getAuthor().getRegistered());
    // verifying category
    assertNotNull("validate saved post category not null", savedPost.getCategory());
    assertEquals("validate correct saved post category name", "sub category", savedPost.getCategory().getName());
    assertEquals("validate correct saved post category parent id", 1l, savedPost.getCategory().getParentId().longValue());
    // verifying values
    assertEquals("validate correct saved post content", "Some fresh new content...", savedPost.getContent());
    assertNotNull("validate saved post content has created date", savedPost.getCreated());
    assertNotNull("validate saved post content has modified date", savedPost.getModified());
    assertEquals("validaste correct saved post status", PostStatus.PUBLIC, savedPost.getStatus());
    assertEquals("validaste correct saved post title", "Fresh out of the box", savedPost.getTitle());
    // verifying tags
    assertNotNull("validate saved post tags not null", savedPost.getTags());
    assertEquals("validate saved post tags size", 1, savedPost.getTags().size());
    assertEquals("validate saved post tag name", "fancy", savedPost.getTags().stream().findFirst().get().getName());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdatePost_withValidPost_shouldCreateAndDeleteThenPost()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    Date freshDate = new Date();
    PostDTO savedPost = service.getPost(1l);
    savedPost.setAuthor(getTestUser(2l));
    savedPost.setCategory(getTestCategory(1l));
    savedPost.setContent("Some updated new content...");
    savedPost.setCreated(freshDate);
    savedPost.setModified(freshDate);
    savedPost.setStatus(PostStatus.MAINTENANCE);
    savedPost.setTitle("Updated out of the box");
    savedPost.setTags(ImmutableList.of(getTag(3l)));
    service.updatePost(savedPost);

    PostDTO updatedPost = service.getPost(savedPost.getPostId());

    assertNotNull("validate updated post not null", updatedPost);
    // verifying author
    assertNotNull("validate updated post author not null", updatedPost.getAuthor());
    assertEquals("validate updated post author email", "no@body.cc", updatedPost.getAuthor().getEmail());
    assertEquals("validate updated post author nickname", "body", updatedPost.getAuthor().getNickName());
    assertNotNull("validate updated post author has registered date", updatedPost.getAuthor().getRegistered());
    // verifying category
    assertNotNull("validate updated post category not null", updatedPost.getCategory());
    assertEquals("validate updated post category name", "main category", updatedPost.getCategory().getName());
    assertNull("validate updated post category parent id", updatedPost.getCategory().getParentId());
    // verifying values
    assertEquals("validate updated post content", "Some updated new content...", updatedPost.getContent());
    assertNotNull("validate updated post content has created date", updatedPost.getCreated());
    assertNotNull("validate updated post content has modified date", updatedPost.getModified());
    assertEquals("validaste updated post status", PostStatus.MAINTENANCE, updatedPost.getStatus());
    assertEquals("validaste updated post title", "Updated out of the box", updatedPost.getTitle());
    // verifying tags
    assertNotNull("validate updated post tags not null", updatedPost.getTags());
    assertEquals("validate updated post tags size", 1l, updatedPost.getTags().size());
    assertTrue("validate updated post tag name", updatedPost.getTags().stream().anyMatch(tag -> tag.getName().equals("guide")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testDeletePost_withValidPost_shouldCreateAndDeleteThenPost()
      throws DuplicateEntityException, EntityIsNotExistingException {
    service.deletePost(1l);
    assertFalse(dao.exists(1l));
  }

  @Test(expected = DependencyNotFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreatePost_withPostWithInvalidNonExistingUserOnCreate_shouldThrowException()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    Date freshDate = new Date();
    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(123l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    service.createPost(freshPost).getPostId();
  }

  @Test(expected = DependencyNotFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdatePost_withPostWithInvalidNonExistingUserOnUpdate_shouldThrowException()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    Date freshDate = new Date();
    PostDTO savedPost = service.getPost(1l);
    savedPost.setAuthor(getTestUser(232l));
    savedPost.setCategory(getTestCategory(1l));
    savedPost.setContent("Some updated new content...");
    savedPost.setCreated(freshDate);
    savedPost.setModified(freshDate);
    savedPost.setStatus(PostStatus.MAINTENANCE);
    savedPost.setTitle("Updated out of the box");
    savedPost.setTags(ImmutableList.of(getTag(1l), getTag(2l)));

    service.updatePost(savedPost);
  }

  @Test(expected = DependencyNotFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreatePost_withPostWithInvalidNonExistingCategoryOnCreate_shouldThrowException()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    Date freshDate = new Date();
    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(254l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    service.createPost(freshPost).getPostId();
  }

  @Test(expected = DependencyNotFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdatePost_withPostWithInvalidNonExistingCategoryOnUpdate_shouldThrowException()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    Date freshDate = new Date();
    PostDTO savedPost = service.getPost(1l);
    savedPost.setAuthor(getTestUser(2l));
    savedPost.setCategory(getTestCategory(156l));
    savedPost.setContent("Some updated new content...");
    savedPost.setCreated(freshDate);
    savedPost.setModified(freshDate);
    savedPost.setStatus(PostStatus.MAINTENANCE);
    savedPost.setTitle("Updated out of the box");
    savedPost.setTags(ImmutableList.of(getTag(1l), getTag(2l)));

    service.updatePost(savedPost);
  }

  @Test(expected = DependencyNotFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreatePost_withPostWithInvalidNonExistingTagOnCreate_shouldThrowException()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    Date freshDate = new Date();
    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("Fresh out of the box");
    freshPost.setTags(ImmutableList.of(getTag(154l)));

    // create new Post
    Long freshId = service.createPost(freshPost).getPostId();

    service.getPost(freshId);
  }

  @Test(expected = DependencyNotFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdatePost_withPostWithInvalidNonExistingTagOnUpdate_shouldThrowException()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    Date freshDate = new Date();
    PostDTO savedPost = service.getPost(1l);
    savedPost.setAuthor(getTestUser(2l));
    savedPost.setCategory(getTestCategory(1l));
    savedPost.setContent("Some updated new content...");
    savedPost.setCreated(freshDate);
    savedPost.setModified(freshDate);
    savedPost.setStatus(PostStatus.MAINTENANCE);
    savedPost.setTitle("Updated out of the box");
    savedPost.setTags(ImmutableList.of(getTag(1345l), getTag(2l)));

    service.updatePost(savedPost);
  }

  @Test(expected = DuplicateEntityException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreatePost_withPostWithAlreadyExistingTitle_shouldThrowException()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    Date freshDate = new Date();

    PostDTO freshPost = new PostDTO();
    freshPost.setAuthor(getTestUser(1l));
    freshPost.setCategory(getTestCategory(2l));
    freshPost.setContent("Some fresh new content...");
    freshPost.setCreated(freshDate);
    freshPost.setModified(freshDate);
    freshPost.setStatus(PostStatus.PUBLIC);
    freshPost.setTitle("somethings");
    freshPost.setTags(ImmutableList.of(getTag(1l)));

    service.createPost(freshPost).getPostId();
  }

  @Test(expected = DuplicateEntityException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdatePost_withPostWithAlreadyExistingTitle_shouldThrowException()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    PostDTO savedPost = service.getPost(2l);
    savedPost.setTitle("somethings");

    service.updatePost(savedPost);
  }

  @Test(expected = EntityIsNotExistingException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testDelete_withNonExitingId_shouldThrowException()
      throws EntityIsNotExistingException {
    service.deletePost(3423543l);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  @Transactional
  public void testRewind_withExistingPrevVersion_shouldRewind() throws NoVersionFoundException,
      DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
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

    assertNotNull("validate before rewind post not null", updatedPost);
    // verifying author
    assertNotNull("validate before rewind post author not null", updatedPost.getAuthor());
    assertEquals("validate before rewind post author email", "no@body.cc", updatedPost.getAuthor().getEmail());
    assertEquals("validate before rewind post author nickname", "body", updatedPost.getAuthor().getNickName());
    assertNotNull("validate before rewind post author has registered date", updatedPost.getAuthor().getRegistered());
    // verifying category
    assertNotNull("validate before rewind post category not null", updatedPost.getCategory());
    assertEquals("validate before rewind post category name", "main category", updatedPost.getCategory().getName());
    assertNull("validate before rewind post category parent id", updatedPost.getCategory().getParentId());
    // verifying values
    assertEquals("validate before rewind post content", "Some updated new content...", updatedPost.getContent());
    assertNotNull("validate before rewind post has created date", updatedPost.getCreated());
    assertNotNull("validate before rewind post has modified date", updatedPost.getModified());
    assertEquals("validate before rewind post status", PostStatus.MAINTENANCE, updatedPost.getStatus());
    assertEquals("validate before rewind post title", "Updated out of the box", updatedPost.getTitle());
    // verifying tags
    assertNotNull("validate before rewind post tags not null", updatedPost.getTags());
    assertEquals("validate before rewind post tags count", 2, updatedPost.getTags().size());
    assertTrue("validate before rewind post first tag name", updatedPost.getTags().stream().anyMatch(tag -> tag.getName().equals("fancy")));
    assertTrue("validate before rewind post second tag name", updatedPost.getTags().stream().anyMatch(tag -> tag.getName().equals("guide")));

    service.rewind(savedPost.getPostId());

    PostDTO rewindPost = service.getPost(savedPost.getPostId());

    assertNotNull("validate after rewind post not null", rewindPost);
    // verifying author
    assertNotNull("validate after rewind post author not null", rewindPost.getAuthor());
    assertEquals("validate after rewind post author email", "flo@kinger.cc", rewindPost.getAuthor().getEmail());
    assertEquals("validate after rewind post author nickname", "daflo", rewindPost.getAuthor().getNickName());
    assertNotNull("validate after rewind post author has registered date", rewindPost.getAuthor().getRegistered());
    // verifying category
    assertNotNull("validate after rewind post category not null", rewindPost.getCategory());
    assertEquals("validate after rewind post category name", "main category", rewindPost.getCategory().getName());
    assertNull("validate after rewind post category parent id", rewindPost.getCategory().getParentId());
    // verifying values
    assertEquals("validate after rewind post content", "some content...", rewindPost.getContent());
    assertNotNull("validate after rewind post has created date", rewindPost.getCreated());
    assertNotNull("validate after rewind post has modified date", rewindPost.getModified());
    assertEquals("validate after rewind post status", PostStatus.PUBLIC, rewindPost.getStatus());
    assertEquals("validate after rewind post title", "somethings", rewindPost.getTitle());
    // verifying tags
    assertNotNull("validate after rewind post tags not null", rewindPost.getTags());
    assertEquals("validate after rewind post tags count", 1, rewindPost.getTags().size());
  }

  @Test(expected = NoVersionFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testRewind_withNoPreviousVersion_shouldThrowException()
      throws NoVersionFoundException, DuplicateEntityException, EntityIsNotExistingException {
    service.rewind(1l);
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

  private Sort createSort(Direction direction, String sortBy) {
    return new Sort(direction, sortBy);
  }
}
