package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

    assertNotNull(categorys);
    assertTrue(categorys.size() == 0);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetAllCategorys_shouldReturnAll() {
    List<CategoryDTO> categorys = service.getAllCategories();

    assertNotNull(categorys);
    assertTrue(categorys.size() == 2);
    assertTrue(categorys.stream().anyMatch(category -> category.getName().equals("main category")));
    assertTrue(categorys.stream().anyMatch(category -> category.getName().equals("sub category")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetCategory_withValidId_shouldReturnCorrectCategory()
      throws EntityIsNotExistingException {
    CategoryDTO categoryPost = service.getCategory(1l);

    assertNotNull(categoryPost);
    assertEquals("main category", categoryPost.getName());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetCategory_withValidIdAndParent_shouldReturnCorrectCategory()
      throws EntityIsNotExistingException {
    CategoryDTO categoryPost = service.getCategory(2l);

    assertNotNull(categoryPost);
    assertEquals("sub category", categoryPost.getName());

    assertNotNull(categoryPost.getParentId());
    assertTrue(categoryPost.getParentId() == 1l);
    assertTrue(categoryPost.getRank() == 1);
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
    assertNotNull(categories);
    assertTrue(categories.size() == 1);
    assertTrue(categories.stream().anyMatch(cat -> cat.getName().equals("main category")));
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testGetCategoriesFromParent_withInValidParent_shouldReturnEmpty()
      throws EntityIsNotExistingException {
    List<CategoryDTO> categories = service.getCategoriesFromParent(646546546l);
    assertNotNull(categories);
    assertTrue(categories.size() == 0);
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testCreateCategory_withValidName_shouldWork()
      throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
    CategoryDTO category = new CategoryDTO();
    category.setName("improved category");
    category.setRank(2);
    category.setParentId(2l);

    Long newCategoryId = service.createCategory(category).getCategoryId();

    CategoryDTO newCategory = map(dao.findOne(newCategoryId));
    assertNotNull(newCategory);
    assertEquals("improved category", newCategory.getName());
    assertTrue(newCategory.getRank() == 2);
    assertTrue(newCategory.getParentId() == 2l);
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
    assertNotNull(newCategory);
    assertEquals("improved category", newCategory.getName());
    assertTrue(newCategory.getRank() == 2);
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
    service.updateCategory(newCategory);

    CategoryDTO updatedCategory = map(dao.findOne(1l));
    assertNotNull(updatedCategory);
    assertEquals("new better name", updatedCategory.getName());
    assertTrue(updatedCategory.getRank() == 3);
    assertTrue(updatedCategory.getParentId() == 1l);
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

    assertTrue(service.getAllCategories().size() == 2);
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
    assertEquals("fancy active category", updatedCategory.getName());
    assertTrue(updatedCategory.getParentId() == null);
    assertTrue(updatedCategory.getRank() == 1234);

    service.rewind(2l);
    CategoryDTO rewindedCategory = map(dao.findOne(2l));
    assertEquals("sub category", rewindedCategory.getName());
    assertTrue(rewindedCategory.getParentId() == 1l);
    assertTrue(rewindedCategory.getRank() == 2);
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
