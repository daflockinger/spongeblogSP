package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.flockinger.spongeblogSP.dao.CategoryDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.CategoryPostsDTO;
import com.flockinger.spongeblogSP.dto.link.CategoryLink;
import com.flockinger.spongeblogSP.dto.link.PostLink;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
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
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetAllCategorys_shouldReturnAll() {
		List<CategoryDTO> categorys = service.getAllCategories();

		assertNotNull(categorys);
		assertTrue(categorys.size() == 2);
		assertTrue(categorys.stream().anyMatch(category -> category.getName().equals("main category")));
		assertTrue(categorys.stream().anyMatch(category -> category.getName().equals("sub category")));
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetCategory_withValidId_shouldReturnCorrectCategory() throws EntityIsNotExistingException {
		CategoryPostsDTO categoryPost = service.getCategory(1l);

		assertNotNull(categoryPost);
		assertEquals("main category", categoryPost.getName());
		List<PostLink> links = categoryPost.getPosts();
		assertNotNull(links);
		assertTrue(links.size() == 3);
		assertTrue(links.stream().anyMatch(link -> link.getId() == 1));
		assertTrue(links.stream().anyMatch(link -> link.getId() == 3));
		assertTrue(links.stream().anyMatch(link -> link.getId() == 5));

		assertTrue(categoryPost.getSubCategories().size() == 1);
		CategoryLink subCategory = categoryPost.getSubCategories().stream().findFirst().get();
		assertTrue(subCategory.getId() == 2l);
		assertTrue(subCategory.getRank() == 1);
	}

	@Test(expected = EntityIsNotExistingException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetCategory_withNotValidId_shouldReturnCorrectCategory() throws EntityIsNotExistingException {
		service.getCategory(176576l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetCategoriesFromParent_withValidParent_shouldReturnCorrect() throws EntityIsNotExistingException {
		List<CategoryPostsDTO> categories = service.getCategoriesFromParent(null);
		assertNotNull(categories);
		assertTrue(categories.size() == 1);
		assertTrue(categories.stream().anyMatch(cat -> cat.getName().equals("main category")));
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetCategoriesFromParent_withInValidParent_shouldReturnEmpty() throws EntityIsNotExistingException {
		List<CategoryPostsDTO> categories = service.getCategoriesFromParent(646546546l);
		assertNotNull(categories);
		assertTrue(categories.size() == 0);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testCreateCategory_withValidName_shouldWork()
			throws DuplicateEntityException, EntityIsNotExistingException {
		CategoryDTO category = new CategoryDTO();
		category.setName("improved category");
		category.setRank(2);
		category.setParentId(2l);

		Long newCategoryId = service.createCategory(category).getId();

		CategoryDTO newCategory = map(dao.findOne(newCategoryId));
		assertNotNull(newCategory);
		assertEquals("improved category", newCategory.getName());
		assertTrue(newCategory.getRank() == 2);
		assertTrue(newCategory.getParentId() == 2l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdateCategory_withValidName_shouldWork()
			throws DuplicateEntityException, EntityIsNotExistingException {

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
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testDeleteCategory_withValidNameAndChildren_shouldDeleteCategoryWithChildren()
			throws DuplicateEntityException, EntityIsNotExistingException {
		CategoryDTO category = new CategoryDTO();
		category.setName("improved category");
		category.setRank(2);
		category.setParentId(2l);

		Long newCategoryId = service.createCategory(category).getId();
		
		CategoryDTO newSub1 = new CategoryDTO();
		newSub1.setParentId(newCategoryId);
		newSub1.setName("best new sub");
		newSub1.setRank(13);
		service.createCategory(newSub1).getId();
		CategoryDTO newSub2 = new CategoryDTO();
		newSub2.setParentId(newCategoryId);
		newSub2.setName("even better sub");
		newSub2.setRank(123);
		service.createCategory(newSub2).getId();

		service.deleteCategory(newCategoryId);

		assertTrue(service.getAllCategories().size() == 2);
	}
	

	@Test(expected = DataIntegrityViolationException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testDeleteCategory_withCategoryWithPosts_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException {
		service.deleteCategory(1l);
	}

	@Test(expected = ConstraintViolationException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testCreateCategory_withEmptyName_shouldThrowException()
			throws ConstraintViolationException, DuplicateEntityException {
		CategoryDTO category = new CategoryDTO();

		service.createCategory(category);
	}

	@Test(expected = EntityIsNotExistingException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testDeleteCategory_withNotValidId_shouldDeleteCategory() throws EntityIsNotExistingException {
		service.deleteCategory(23434234l);
	}

	private CategoryDTO map(Category category) {
		return mapper.map(category, CategoryDTO.class);
	}
}
