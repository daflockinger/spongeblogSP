package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.CategoryPostsDTO;
import com.flockinger.spongeblogSP.dto.link.CategoryLink;
import com.flockinger.spongeblogSP.dto.link.PostLink;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_CLASS)
public class CategoryServiceTest {

	@Autowired
	private CategoryService service;
	
	@Test
	public void testGetAllCategorys_shouldReturnAll(){
		List<CategoryDTO> categorys = service.getAllCategories();
		
		assertNotNull(categorys);
		assertTrue(categorys.size() == 2);
		assertTrue(categorys.stream().anyMatch(category -> category.getName().equals("main category")));
		assertTrue(categorys.stream().anyMatch(category -> category.getName().equals("sub category")));
	}
	
	@Test
	public void testGetCategory_withValidId_shouldReturnCorrectCategory() throws EntityIsNotExistingException{
		CategoryPostsDTO categoryPost = service.getCategory(1l);
		
		assertNotNull(categoryPost);
		assertEquals("main category", categoryPost.getName());
		List<PostLink> links = categoryPost.getPosts();
		assertNotNull(links);
		assertTrue(links.size() == 1);
		assertTrue(links.stream().anyMatch(link -> link.getId() == 1));
		
		assertTrue(categoryPost.getSubCategories().size() == 1);
		CategoryLink subCategory = categoryPost.getSubCategories().stream().findFirst().get();
		assertTrue(subCategory.getId() == 2l);
		assertTrue(subCategory.getRank() == 1);
	}
	
	@Test(expected=EntityIsNotExistingException.class)
	public void testGetCategory_withNotValidId_shouldReturnCorrectCategory() throws EntityIsNotExistingException{
		service.getCategory(176576l);
	}
	
	@Test
	public void testGetCategoriesFromParent_withValidParent_shouldReturnCorrect() throws EntityIsNotExistingException{
		List<CategoryPostsDTO> categories = service.getCategoriesFromParent(null);
		assertNotNull(categories);
		assertTrue(categories.size() == 1);
		assertTrue(categories.stream().anyMatch(cat -> cat.getName().equals("main category")));
	}
	
	@Test
	public void testGetCategoriesFromParent_withInValidParent_shouldReturnEmpty() throws EntityIsNotExistingException{
		List<CategoryPostsDTO> categories = service.getCategoriesFromParent(646546546l);
		assertNotNull(categories);
		assertTrue(categories.size() == 0);
	}
	
	@Test
	public void testCreateUpdateAndDeleteCategory_withValidName_shouldWork() throws DuplicateEntityException, EntityIsNotExistingException{
		
		// create new category
		CategoryDTO category = new CategoryDTO();
		category.setName("improved category");
		category.setRank(2);
		category.setParentId(2l);
		
		Long newCategoryId = service.createCategory(category).getId();
		
		// create sub category for that
		CategoryDTO newSub = new CategoryDTO();
		newSub.setName("new little sub");
		newSub.setParentId(newCategoryId);
		service.createCategory(newSub);
		
	    CategoryDTO newCategory = service.getAllCategories().stream().filter(cat -> cat.getName().equals("improved category")).findAny().get();
	    //verify created category
	    assertNotNull(newCategory);
	    assertEquals("improved category", newCategory.getName());
	    assertTrue(newCategory.getRank() == 2);
	    assertTrue(newCategory.getParentId() == 2l);
	    
		//create new children
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
		
		//update the category
	    newCategory.setRank(3);
		newCategory.setName("new better name");
		newCategory.setParentId(1l);
		service.updateCategory(newCategory);
		
		CategoryDTO updatedCategory = service.getAllCategories().stream().filter(cat -> cat.getName().equals("new better name")).findAny().get();
	    
	    //verify updates
	    assertNotNull(updatedCategory);
	    assertEquals("new better name", updatedCategory.getName());
	    assertTrue(updatedCategory.getRank() == 3);
	    assertTrue(updatedCategory.getParentId() == 1l);
	    
	    //verify attached children
	    assertTrue(service.getAllCategories().size() == 6);
	    
	    //delete new category inclusive the sub category
	    service.deleteCategory(newCategoryId);
		// verify cascaded deletion
		assertTrue(service.getAllCategories().size() == 2);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void testCreateCategory_withEmptyName_shouldThrowException() throws ConstraintViolationException, DuplicateEntityException{
		CategoryDTO category = new CategoryDTO();
		
		service.createCategory(category);
	}
	
	
	@Test(expected=EntityIsNotExistingException.class)
	public void testDeleteCategory_withNotValidId_shouldDeleteCategory() throws EntityIsNotExistingException{
		service.deleteCategory(23434234l);
	}
	
}
