package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.spongeblogSP.dao.CategoryDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.exception.OrphanedDependingEntitiesException;
import com.flockinger.spongeblogSP.model.Category;

public class CategoryServiceTest extends BaseServiceTest {

  @Autowired
  private CategoryService service;

  @Autowired
  private CategoryDAO dao;

  @Autowired
  private ModelMapper mapper;

  @Test
  @FlywayTest(invokeCleanDB = true)
  public void testGetAllCategorys__withEmptyDB_shouldReturnEmpty() {
    List<CategoryDTO> categorys = service.getAllCategories();

    assertNotNull("validate returned categories not null", categorys);
    assertEquals("validate correct category count", 0, categorys.size());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllCategorys_shouldReturnAll() {
    List<CategoryDTO> categorys = service.getAllCategories();

    assertNotNull("validate returned categories not null", categorys);
    assertEquals("validate correct category count", 3, categorys.size());
    assertTrue("validate category with name exists",
        categorys.stream().anyMatch(category -> category.getName().equals("main category")));
    assertTrue("validate category with name exists",
        categorys.stream().anyMatch(category -> category.getName().equals("sub category")));
    assertTrue("validate category with specific page id exists", categorys.stream()
        .anyMatch(category -> category.getPageId() != null && category.getPageId() == 8));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetCategory_withValidId_shouldReturnCorrectCategory()
      throws EntityIsNotExistingException {
    CategoryDTO categoryPost = service.getCategory(1l);

    assertNotNull("validate returned category post is not null", categoryPost);
    assertEquals("validate correct saved category name", "main category", categoryPost.getName());
    assertEquals("validate correct saved category children count", 2,
        categoryPost.getChildren().size());
    assertTrue("validate correct saved categories first sub category name", 
        categoryPost.getChildren().stream().anyMatch(c -> c.getName().equals("sub category")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetCategory_withValidIdAndParent_shouldReturnCorrectCategory()
      throws EntityIsNotExistingException {
    CategoryDTO categoryPost = service.getCategory(2l);

    assertNotNull("validate category not null", categoryPost);
    assertEquals("validate correct categories sub category name", "sub category",
        categoryPost.getName());
    assertEquals("validate correct categories parent ID", 1l,
        categoryPost.getParentId().longValue());
    assertEquals("validate correct category rank", 1l, categoryPost.getRank().longValue());
  }

  @Test(expected = EntityIsNotExistingException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetCategory_withNotValidId_shouldReturnCorrectCategory()
      throws EntityIsNotExistingException {
    service.getCategory(176576l);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetCategoriesFromParent_withValidParent_shouldReturnCorrect()
      throws EntityIsNotExistingException {
    List<CategoryDTO> categories = service.getCategoriesFromParent(null);
    assertNotNull("validate category from null parent not null", categories);
    assertTrue("validate correct category from null parent size", categories.size() == 1);
    assertTrue("validate correct category fron null parent name",
        categories.stream().anyMatch(cat -> cat.getName().equals("main category")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetCategoriesFromParent_withInValidParent_shouldReturnEmpty()
      throws EntityIsNotExistingException {
    List<CategoryDTO> categories = service.getCategoriesFromParent(646546546l);
    assertNotNull("validate not found categories from parent not null", categories);
    assertTrue("validate not found categories from parent is empty", categories.size() == 0);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateCategory_withValidName_shouldWork()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    CategoryDTO category = new CategoryDTO();
    category.setName("improved category");
    category.setRank(2);
    category.setParentId(2l);
    category.setPageId(23l);

    Long newCategoryId = service.createCategory(category).getCategoryId();

    CategoryDTO newCategory = map(dao.findOne(newCategoryId));
    assertNotNull("validate new returned category is not null", newCategory);
    assertEquals("validate new created category name", "improved category", newCategory.getName());
    assertEquals("validate new created category rank", 2, newCategory.getRank().intValue());
    assertEquals("validate new created category parent id", 2l,
        newCategory.getParentId().longValue());
    assertEquals("validate new created category pageID", 23l, newCategory.getPageId().longValue());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateCategory_withNullParent_shouldWork()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    CategoryDTO category = new CategoryDTO();
    category.setName("improved category");
    category.setRank(2);
    category.setParentId(null);

    Long newCategoryId = service.createCategory(category).getCategoryId();

    CategoryDTO newCategory = map(dao.findOne(newCategoryId));
    assertNotNull("validate created null parent category is not null", newCategory);
    assertEquals("validate created null parent category correct name", "improved category",
        newCategory.getName());
    assertEquals("validate created null parent category correct rank", 2,
        newCategory.getRank().intValue());
  }

  @Test(expected = DependencyNotFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateCategory_withNonExistingParent_shouldWork()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    CategoryDTO category = new CategoryDTO();
    category.setName("improved category");
    category.setRank(2);
    category.setParentId(765765l);

    service.createCategory(category).getCategoryId();
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testUpdateCategory_withValidName_shouldWork()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {

    CategoryDTO newCategory = map(dao.findOne(1l));
    newCategory.setRank(3);
    newCategory.setName("new better name");
    newCategory.setParentId(1l);
    newCategory.setPageId(23l);
    service.updateCategory(newCategory);

    CategoryDTO updatedCategory = map(dao.findOne(1l));
    assertNotNull("validate updated category not null", updatedCategory);
    assertEquals("validate correct updated category name", "new better name",
        updatedCategory.getName());
    assertEquals("validate correct updated category name", 3, updatedCategory.getRank().intValue());
    assertEquals("validate correct updated category name", 1l,
        updatedCategory.getParentId().longValue());
    assertEquals("validate correct updated category name", 23l,
        updatedCategory.getPageId().longValue());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testDeleteCategory_withValidNameAndChildren_shouldDeleteCategoryWithChildren()
      throws DuplicateEntityException, EntityIsNotExistingException,
      OrphanedDependingEntitiesException, DependencyNotFoundException {
    CategoryDTO category = new CategoryDTO();
    category.setName("improved category");
    category.setRank(2);
    category.setParentId(2l);

    Long newCategoryId = service.createCategory(category).getCategoryId();

    CategoryDTO newSub1 = new CategoryDTO();
    newSub1.setParentId(newCategoryId);
    newSub1.setName("best new sub");
    newSub1.setRank(13);
    service.createCategory(newSub1).getCategoryId();
    CategoryDTO newSub2 = new CategoryDTO();
    newSub2.setParentId(newCategoryId);
    newSub2.setName("even better sub");
    newSub2.setRank(123);
    service.createCategory(newSub2).getCategoryId();

    service.deleteCategory(newCategoryId);

    assertEquals("validate category size after deletion", 3, service.getAllCategories().size());
  }


  @Test(expected = OrphanedDependingEntitiesException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testDeleteCategory_withCategoryWithPosts_shouldThrowException()
      throws DuplicateEntityException, EntityIsNotExistingException,
      OrphanedDependingEntitiesException {
    service.deleteCategory(1l);
  }



  @Test(expected = EntityIsNotExistingException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testDeleteCategory_withNotValidId_shouldDeleteCategory()
      throws EntityIsNotExistingException, OrphanedDependingEntitiesException {
    service.deleteCategory(23434234l);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testRewind_withExistingPrevVersion_shouldRewind() throws NoVersionFoundException,
      DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    CategoryDTO oldCategory = map(dao.findOne(2l));
    oldCategory.setRank(2);
    service.updateCategory(oldCategory);

    CategoryDTO category = map(dao.findOne(2l));
    category.setCategoryId(2l);
    category.setName("fancy active category");
    category.setParentId(null);
    category.setRank(1234);
    service.updateCategory(category);

    CategoryDTO updatedCategory = map(dao.findOne(2l));
    assertEquals("validate category name before rewind", "fancy active category",
        updatedCategory.getName());
    assertNull("validate category parentId before rewind", updatedCategory.getParentId());
    assertEquals("validate category rank before rewind", 1234,
        updatedCategory.getRank().intValue());

    service.rewind(2l);
    CategoryDTO rewindedCategory = map(dao.findOne(2l));
    assertEquals("validate category name before rewind", "sub category",
        rewindedCategory.getName());
    assertEquals("validate category parentId before rewind", 1l,
        rewindedCategory.getParentId().longValue());
    assertEquals("validate category rank before rewind", 2, rewindedCategory.getRank().intValue());
  }

  @Test(expected = NoVersionFoundException.class)
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testRewind_withNoPreviousVersion_shouldThrowException()
      throws NoVersionFoundException, DuplicateEntityException, EntityIsNotExistingException {
    service.rewind(2l);
  }

  private CategoryDTO map(Category category) {
    return mapper.map(category, CategoryDTO.class);
  }
}
