package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostPreviewDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;

public class PageServiceTest extends BaseServiceTest {

  @Autowired
  private PageService service;

  @MockBean
  private PostService mockPostService;

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetPages_withoutCategory_shouldReturnCorrect() {
    List<PostPreviewDTO> pages = service.getPages(true);
    assertNotNull("validate post page not null", pages);
    assertEquals("validate correct page count", 1, pages.size());
    assertEquals("validate correct page id", 7l,
        pages.stream().findFirst().get().getPostId().longValue());
    assertEquals("validate correct page content", "",
        pages.stream().findFirst().get().getPartContent());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetPages_withCategory_shouldReturnCorrect() {
    List<PostPreviewDTO> pages = service.getPages(false);
    assertNotNull("validate post page not null", pages);
    assertEquals("validate correct page count", 1, pages.size());
    assertEquals("validate correct page id", 8l,
        pages.stream().findFirst().get().getPostId().longValue());
    assertEquals("validate correct page content", "",
        pages.stream().findFirst().get().getPartContent());
  }

  @Test
  public void testDeletePost_withId_shouldCallPostServiceDeleteCorrectly()
      throws DuplicateEntityException, EntityIsNotExistingException {
    service.deletePage(23l);
    verify(mockPostService).deletePost(23l);
  }

  @Test
  public void testRewind_withId_shouldCallPostServiceDeleteCorrectly()
      throws NoVersionFoundException {
    service.rewind(23l);
    verify(mockPostService).rewind(23l);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetPage_withExistingPage_shouldReturnCorrect()
      throws EntityIsNotExistingException {
    service.getPage(8l);
    verify(mockPostService).getPost(anyLong());
  }


  @Test
  public void testCreatePage_withValidPage_shouldCallPostServiceCreatePost()
      throws DuplicateEntityException, DependencyNotFoundException {
    service.createPage(new PostDTO());
    verify(mockPostService).createPost(any(PostDTO.class));
  }

  @Test
  public void testUpdatePage_withValidPage_shouldCallPostServiceUpdatePost()
      throws EntityIsNotExistingException, DuplicateEntityException, DependencyNotFoundException {
    service.updatePage(new PostDTO());
    verify(mockPostService).updatePost(any(PostDTO.class));
  }
}
