package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.spongeblogSP.dao.TagDAO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;

public class TagServiceTest extends BaseServiceTest {

  @Autowired
  private TagService service;

  @Autowired
  private TagDAO dao;

  @Test
  @FlywayTest(invokeCleanDB = true)
  public void testGetAllTags__withEmptyDB_shouldReturnEmpty() {
    List<TagDTO> tags = service.getAllTags();

    assertNotNull(tags);
    assertTrue(tags.size() == 0);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllTags_shouldReturnAll() {
    List<TagDTO> tags = service.getAllTags();

    assertNotNull(tags);
    assertTrue(tags.size() == 3);
    assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("fancy")));
    assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("guide")));
    assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("cold")));
  }

  @Test(expected = EntityIsNotExistingException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetTag_withNotValidId_shouldReturnCorrectTag()
      throws EntityIsNotExistingException {
    service.getTag(176576l);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateTag_withValidName_shouldWork()
      throws DuplicateEntityException, EntityIsNotExistingException {
    String tagName = "ordinary";
    Long newTagId = service.createTag(tagName).getTagId();

    TagDTO newTag = service.getTag(newTagId);

    assertNotNull(newTag);
    assertEquals("ordinary", newTag.getName());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdateTag_withValidName_shouldWork()
      throws DuplicateEntityException, EntityIsNotExistingException {

    TagDTO savedTag = new TagDTO();
    savedTag.setTagId(1l);
    savedTag.setName("new better name");
    service.updateTag(savedTag);

    TagDTO updatedTag = service.getTag(1l);

    assertNotNull(updatedTag);
    assertEquals("new better name", updatedTag.getName());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testDeleteTag_withValidName_shouldWork()
      throws DuplicateEntityException, EntityIsNotExistingException {
    service.deleteTag(1l);

    assertFalse(dao.exists(1l));
  }

  @Test(expected = DuplicateEntityException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateTag_withAlreadyExistingName_shouldThrowException()
      throws DuplicateEntityException {
    service.createTag("fancy");
  }

  @Test(expected = ConstraintViolationException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateTag_withTooLongName_shouldThrowException()
      throws ConstraintViolationException, DuplicateEntityException {
    StringBuilder tooLoonTag = new StringBuilder();
    for (int i = 0; i < 151; i++) {
      tooLoonTag.append("a");
    }

    service.createTag(tooLoonTag.toString());
  }

  @Test(expected = DuplicateEntityException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdateTag_withAlreadyExistingName_shouldThrowException()
      throws DuplicateEntityException, EntityIsNotExistingException {
    TagDTO savedTag = new TagDTO();
    savedTag.setTagId(2l);
    savedTag.setName("guide");
    service.updateTag(savedTag);
  }

  @Test(expected = EntityIsNotExistingException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testDeleteTag_withNotValidId_shouldDeleteTag() throws EntityIsNotExistingException {
    service.deleteTag(23434234l);
  }

  @Test
  @FlywayTest(invokeCleanDB = true)
  public void testRewind_withExistingPrevVersion_shouldRewind()
      throws NoVersionFoundException, DuplicateEntityException, EntityIsNotExistingException {
    Long id = service.createTag("guide").getTagId();

    TagDTO tagToUpdate = new TagDTO();
    tagToUpdate.setTagId(id);
    tagToUpdate.setName("fancy guide");
    service.updateTag(tagToUpdate);
    TagDTO updatedTag = service.getTag(id);
    assertEquals("fancy guide", updatedTag.getName());


    service.rewind(id);
    TagDTO rewindTag = service.getTag(id);
    assertEquals("guide", rewindTag.getName());
  }

  @Test(expected = NoVersionFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testRewind_withNoPreviousVersion_shouldThrowException()
      throws NoVersionFoundException, DuplicateEntityException, EntityIsNotExistingException {
    service.rewind(1l);
  }
}
