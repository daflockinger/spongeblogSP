package com.flockinger.spongeblogSP.api;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.spongeblogSP.dao.CategoryDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.model.Category;
import com.flockinger.spongeblogSP.service.CategoryService;

public class CategoryControllerTest extends BaseControllerTest {

	@Autowired
	private CategoryService service;

	@Autowired
	private CategoryDAO dao;

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1CategoriesGet_withFilledDB_shouldReturnAll() throws Exception{
		mockMvc.perform(get("/api/v1/categories")
	               .contentType(jsonContentType))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(2)))
	                
	                .andExpect(jsonPath("$[0].name",is("main category")))
	                .andExpect(jsonPath("$[0].links[0].href",containsString("categories/1")))
	                .andExpect(jsonPath("$[1].links[0].href",containsString("categories/2")))
	                
	                .andExpect(jsonPath("$[1].name",is("sub category")))
	                .andExpect(jsonPath("$[1].links[1].rel",containsString("parent")))
	                .andExpect(jsonPath("$[1].links[1].href",containsString("categories/1")));
	}
	
	@Test
	@FlywayTest(invokeCleanDB=true)
	public void testApiV1CategoriesGet_withEmptyDB_shouldReturnEmpty() throws Exception{
		mockMvc.perform(get("/api/v1/categories")
	               .contentType(jsonContentType))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(0)));
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1CategoriesCategoryIdGet_withValidId_shouldReturnCategory() throws Exception{
		mockMvc.perform(get("/api/v1/categories/2")
	               .contentType(jsonContentType))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.name",is("sub category")))
	                .andExpect(jsonPath("$.rank",is(1)))
	                .andExpect(jsonPath("$._links.self.href",containsString("2")))
	                .andExpect(jsonPath("$._links.parent.href",containsString("1")));
	}
	
	@Test
	@FlywayTest(invokeCleanDB=true)
	public void testApiV1CategoriesCategoryIdGet_withNonExistingId_shouldReturnNotFound() throws Exception{
		mockMvc.perform(get("/api/v1/categories/1")
	               .contentType(jsonContentType))
	                .andExpect(status().isNotFound());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1CategoriesChildrenParentCategoryIdGet_withExistingParent_shouldReturnChildren() throws Exception{
		mockMvc.perform(get("/api/v1/categories/children/1")
	               .contentType(jsonContentType))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(1)))
	                .andExpect(jsonPath("$[0].name",is("sub category")));
	}
	
	@Test
	public void testApiV1CategoriesChildrenParentCategoryIdGet_withNonExistingParent_shouldReturnEmpty() throws Exception{
		mockMvc.perform(get("/api/v1/categories/children/134")
	               .contentType(jsonContentType))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(0)));
	}
	
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1CategoriesCategoryIdDelete_withExisingCategory_shouldDelete() throws Exception{
		Category newCategory = new Category();
		newCategory.setName("fresh Category");
		newCategory.setRank(2);
		newCategory.setParent(dao.findOne(2l));
		Long newCatId = dao.save(newCategory).getId();
		
		mockMvc.perform(delete("/api/v1/categories/" + newCatId)
	               .contentType(jsonContentType))
	                .andExpect(status().isOk());
		
		assertFalse(dao.exists(newCatId));
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1CategoriesCategoryIdDelete_withExisingCategoryWithPosts_shouldReturnConflict() throws Exception{
		mockMvc.perform(delete("/api/v1/categories/1")
	               .contentType(jsonContentType))
	                .andExpect(status().isConflict());
	}
	
	@Test
	public void testApiV1CategoriesCategoryIdDelete_withNonExitingCategory_shouldReturnNotFound() throws Exception{
		mockMvc.perform(delete("/api/v1/categories/134")
	               .contentType(jsonContentType))
	                .andExpect(status().isNotFound());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1CategoriesPost_withValidCategory_shouldCreate() throws Exception{
		CategoryDTO category = new CategoryDTO();
		category.setName("improved category");
		category.setRank(2);
		category.setParentId(2l);
		
		mockMvc.perform(post("/api/v1/categories")
				   .content(json(category))
	               .contentType(jsonContentType))
	                .andExpect(status().isCreated())
	                .andExpect(jsonPath("$.name",is("improved category")))
	                .andExpect(jsonPath("$.rank",is(2)))
	                .andExpect(jsonPath("$._links.self.href",not(empty())))
	                .andExpect(jsonPath("$._links.parent.href",containsString("2")));
	}
	
	@Test
	public void testApiV1CategoriesPost_withCategoryWithNullParent_shouldCreate() throws Exception{
		CategoryDTO category = new CategoryDTO();
		category.setName("improved category");
		category.setRank(2);
		category.setParentId(null);
		
		mockMvc.perform(post("/api/v1/categories")
				   .content(json(category))
	               .contentType(jsonContentType))
	                .andExpect(status().isCreated())
	                .andExpect(jsonPath("$.name",is("improved category")))
	                .andExpect(jsonPath("$.rank",is(2)))
	                .andExpect(jsonPath("$._links.self.href",not(empty())));
	}
	
	@Test
	public void testApiV1CategoriesPost_withCategoryWithNonExistingParent_shouldReturnConflict() throws Exception{
		CategoryDTO category = new CategoryDTO();
		category.setName("improved category");
		category.setRank(2);
		category.setParentId(22343l);
		
		mockMvc.perform(post("/api/v1/categories")
				   .content(json(category))
	               .contentType(jsonContentType))
	                .andExpect(status().isConflict());
	}
	
	@Test
	public void testApiV1CategoriesPost_withCategoryWithEmptyName_shouldReturnBadRequest() throws Exception{
		CategoryDTO category = new CategoryDTO();
		category.setName("");
		category.setRank(2);
		category.setParentId(2l);
		
		mockMvc.perform(post("/api/v1/categories")
				   .content(json(category))
	               .contentType(jsonContentType))
	                .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1CategoriesPost_withCategoryWithNullName_shouldReturnBadRequest() throws Exception{
		CategoryDTO category = new CategoryDTO();
		category.setName(null);
		category.setRank(2);
		category.setParentId(2l);
		
		mockMvc.perform(post("/api/v1/categories")
				   .content(json(category))
	               .contentType(jsonContentType))
	                .andExpect(status().isBadRequest());
	}
	
	
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1CategoriesPut_withValidCategory_shouldUpdate() throws Exception{
		CategoryDTO category =  service.getCategory(2l);
		category.setName("improved category");
		category.setRank(2);
		category.setParentId(null);
		
		mockMvc.perform(put("/api/v1/categories")
				   .content(json(category))
	               .contentType(jsonContentType))
	                .andExpect(status().isOk());
		
		CategoryDTO updatedCategory = service.getCategory(2l);
		assertEquals("improved category",updatedCategory.getName());
		assertTrue(updatedCategory.getRank() == 2);
	}
	
	@Test
	public void testApiV1CategoriesPut_withNonExistingCategory_shouldReturnNotFound() throws Exception{
		CategoryDTO category =  new CategoryDTO();
		category.setCategoryId(32434l);
		category.setName("improved category");
		category.setRank(2);
		category.setParentId(null);
		
		mockMvc.perform(put("/api/v1/categories")
				   .content(json(category))
	               .contentType(jsonContentType))
	                .andExpect(status().isNotFound());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1CategoriesPut_withCategoryWithNonExistingParent_shouldReturnConflict() throws Exception{
		CategoryDTO category =  service.getCategory(2l);
		category.setName("improved category");
		category.setRank(2);
		category.setParentId(2343434l);
		
		mockMvc.perform(put("/api/v1/categories")
				   .content(json(category))
	               .contentType(jsonContentType))
	                .andExpect(status().isConflict());
	}
	
	@Test
	public void testApiV1CategoriesPut_withCategoryWithEmptyName_shouldReturnBadRequest() throws Exception{
		CategoryDTO category = new CategoryDTO();
		category.setName("");
		category.setRank(2);
		category.setParentId(null);
		
		mockMvc.perform(put("/api/v1/categories")
				   .content(json(category))
	               .contentType(jsonContentType))
	                .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1CategoriesPut_withCategoryWithNullName_shouldReturnBadRequest() throws Exception{
		CategoryDTO category = new CategoryDTO();
		category.setName(null);
		category.setRank(2);
		category.setParentId(null);
		
		mockMvc.perform(put("/api/v1/categories")
				   .content(json(category))
	               .contentType(jsonContentType))
	                .andExpect(status().isBadRequest());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1CategoriesRewindCategoryIdPut_withValidVersions_shouldRewind() throws Exception{
		CategoryDTO oldCategory = service.getCategory(2l);
		oldCategory.setRank(2);
		service.updateCategory(oldCategory);

		CategoryDTO category = service.getCategory(2l);
		category.setCategoryId(2l);
		category.setName("fancy active category");
		category.setParentId(null);
		category.setRank(1234);
		service.updateCategory(category);
		
		CategoryDTO updatedCategory =  service.getCategory(2l);
		assertEquals("fancy active category",updatedCategory.getName());
		assertTrue(updatedCategory.getParentId() == null);
		assertTrue(updatedCategory.getRank() == 1234);
		
		mockMvc.perform(put("/api/v1/categories/rewind/2")
	            .contentType(jsonContentType))
	            .andExpect(status().isOk());
		
		
		CategoryDTO rewindedCategory =  service.getCategory(2l);
		assertEquals("sub category",rewindedCategory.getName());
		assertTrue(rewindedCategory.getParentId() == 1l);
		assertTrue(rewindedCategory.getRank() == 2);
		
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1CategoriesRewindCategoryIdPut_withNoVersions_shouldReturnConflict() throws Exception{	
			mockMvc.perform(put("/api/v1/categories/rewind/1")
		            .contentType(jsonContentType))
		            .andExpect(status().isConflict());
	}
}
