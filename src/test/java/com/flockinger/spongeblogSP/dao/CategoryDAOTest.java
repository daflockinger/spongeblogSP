package com.flockinger.spongeblogSP.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import com.flockinger.spongeblogSP.model.Category;
import com.flockinger.spongeblogSP.service.BaseServiceTest;


public class CategoryDAOTest extends BaseServiceTest{
	
	@Autowired
	private TestService dao;
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdate_WithCategoryWithoutRelatives_ShouldWork(){
		dao.update(getTestUpdatedCategory());
		
		Category saved = dao.findOne(1l);
		assertNotNull(saved);
		assertEquals("main category",saved.getName());
		assertTrue(saved.getRank() == 1);
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" },invokeCleanDB=true)
	public void testUpdate_WithCategoryWithOnlyChildren_ShouldWork(){		
		Category kid1 = new Category();
		kid1.setName("some child");
		kid1.setRank(3);
		kid1.setParent(dao.findOne(2l));
		dao.save(kid1);
		Category kid2 = new Category();
		kid2.setName("another kid");
		kid2.setRank(4);
		kid2.setParent(dao.findOne(2l));
		dao.save(kid2);
						
		dao.update(getTestUpdatedCategory());
				
		Category saved = dao.findOne(2l);
		assertNotNull(saved);
		assertEquals("changed name",saved.getName());
		assertTrue(saved.getRank() == 23);
				
		assertTrue(saved.getSubCategories().size() == 2);
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getName().equals("some child")));
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getName().equals("another kid")));
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getRank() == 3));
		assertTrue(saved.getSubCategories().stream().anyMatch(kid -> kid.getRank() == 4));
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdate_WithCategoryWithOnlyParent_ShouldWork(){
		Category category = getTestUpdatedCategory();
		category.setParent( dao.findOne(1l));
		
		dao.update(category);
				
		Category saved =  dao.findOne(2l);
		assertNotNull(saved);
		assertEquals("changed name",saved.getName());
		assertTrue(saved.getRank() == 23);
		
		assertNotNull(category.getParent());
		assertEquals("main category", category.getParent().getName());
		assertTrue(category.getParent().getRank() == 1);
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdate_WithCategoryWithParentAndChildren_ShouldWork(){
		Category kid1 = new Category();
		kid1.setName("some child");
		kid1.setRank(3);
		kid1.setParent(dao.findOne(2l));
		dao.save(kid1);
		Category kid2 = new Category();
		kid2.setName("another kid");
		kid2.setRank(4);
		kid2.setParent(dao.findOne(2l));
		dao.save(kid2);
		
		Category category = getTestUpdatedCategory();
		category.setParent(dao.findOne(1l));
		
		dao.update(category);
				
		
		Category saved = dao.findOne(2l);
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
	}
	
	@Test(expected=JpaObjectRetrievalFailureException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdate_WithCategoryWithNotExistingParent_ShouldThrowException(){
		Category absentParent = new Category();
		absentParent.setId(23476l);
		Category category = getTestUpdatedCategory();
		category.setParent(absentParent);
		
		dao.update(category);
		
	}
	
	private Category getTestUpdatedCategory(){
		Category category = dao.findOne(2l);
		category.setName("changed name");
		category.setRank(23);
		
		return category;
	}
	
	
}
