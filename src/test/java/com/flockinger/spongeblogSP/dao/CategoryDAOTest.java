package com.flockinger.spongeblogSP.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.flockinger.spongeblogSP.model.Category;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_CLASS)
public class CategoryDAOTest {
	
	@Autowired
	private TestService testService;
	
	private Long createdId;
	
	@Before
	public void setup(){
		Category category = new Category();
		category.setName("brand new");
		category.setRank(-1);
		createdId = testService.save(category).getId();
		
	}
	
	@After
	public void teardown(){
		testService.delete(createdId);
	}
	
	
	@Test
	public void testUpdate_WithCategoryWithoutRelatives_ShouldWork(){
		testService.update(getTestUpdatedCategory());
		
		Category saved = testService.findOne(createdId);
		assertNotNull(saved);
		assertEquals("changed name",saved.getName());
		assertTrue(saved.getRank() == 23);
	}
	
	@Test
	public void testUpdate_WithCategoryWithOnlyChildren_ShouldWork(){		
		Category kid1 = new Category();
		kid1.setName("some child");
		kid1.setRank(3);
		kid1.setParent(testService.findOne(createdId));
		testService.save(kid1);
		Category kid2 = new Category();
		kid2.setName("another kid");
		kid2.setRank(4);
		kid2.setParent(testService.findOne(createdId));
		testService.save(kid2);
						
		testService.update(getTestUpdatedCategory());
				
		Category saved = testService.findOne(createdId);
		assertNotNull(saved);
		assertEquals("changed name",saved.getName());
		assertTrue(saved.getRank() == 23);
				
		assertTrue(saved.getSubCategories().size() == 2);
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getName().equals("some child")));
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getName().equals("another kid")));
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getRank() == 3));
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getRank() == 4));
		
		testService.delete(kid1.getId());
		testService.delete(kid2.getId());
	}
	
	@Test
	public void testUpdate_WithCategoryWithOnlyParent_ShouldWork(){
		Category category = getTestUpdatedCategory();
		category.setParent(testService.findOne(1l));
		
		testService.update(category);
				
		Category saved = testService.findOne(createdId);
		assertNotNull(saved);
		assertEquals("changed name",saved.getName());
		assertTrue(saved.getRank() == 23);
		
		assertNotNull(category.getParent());
		assertEquals("main category", category.getParent().getName());
		assertTrue(category.getParent().getRank() == 1);
	}
	
	@Test
	public void testUpdate_WithCategoryWithParentAndChildren_ShouldWork(){
		Category kid1 = new Category();
		kid1.setName("some child");
		kid1.setRank(3);
		kid1.setParent(testService.findOne(createdId));
		testService.save(kid1);
		Category kid2 = new Category();
		kid2.setName("another kid");
		kid2.setRank(4);
		kid2.setParent(testService.findOne(createdId));
		testService.save(kid2);
		
		Category category = getTestUpdatedCategory();
		category.setParent(testService.findOne(1l));
		
		testService.update(category);
				
		
		Category saved = testService.findOne(createdId);
		assertNotNull(saved);
		assertEquals("changed name",saved.getName());
		assertTrue(saved.getRank() == 23);
				
		assertTrue(saved.getSubCategories().size() == 2);
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getName().equals("some child")));
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getName().equals("another kid")));
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getRank() == 3));
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getRank() == 4));
		
		assertNotNull(category.getParent());
		assertEquals("main category", category.getParent().getName());
		assertTrue(category.getParent().getRank() == 1);
		
		testService.delete(kid1.getId());
		testService.delete(kid2.getId());
	}
	
	@Test(expected=JpaObjectRetrievalFailureException.class)
	public void testUpdate_WithCategoryWithNotExistingParent_ShouldThrowException(){
		Category absentParent = new Category();
		absentParent.setId(23476l);
		Category category = getTestUpdatedCategory();
		category.setParent(absentParent);
		
		testService.update(category);
		
	}
	
	private Category getTestUpdatedCategory(){
		Category category = new Category();
		category.setId(createdId);
		category.setName("changed name");
		category.setRank(23);
		
		return category;
	}
	
	
}
