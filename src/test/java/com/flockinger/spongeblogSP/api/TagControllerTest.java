package com.flockinger.spongeblogSP.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.spongeblogSP.dao.TagDAO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.service.TagService;

public class TagControllerTest extends BaseControllerTest {

	@Autowired
	private TagService service;
	
	@Autowired
	private TagDAO dao;
	
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testapiV1TagsGet_withFullDB_shouldReturnAll() throws Exception {
		mockMvc.perform(get("/api/v1/tags")
	               .contentType(jsonContentType))
					.andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(3)))
	                .andExpect(jsonPath("$[0].name", containsString("fancy")))
	                .andExpect(jsonPath("$[1].name", containsString("cold")))
	                .andExpect(jsonPath("$[2].name", containsString("guide")));
	}
	
	
	@Test
	@FlywayTest(invokeCleanDB=true)
	public void testapiV1TagsGet_withEmptyDB_shouldReturnEmpty() throws Exception {
		mockMvc.perform(get("/api/v1/tags")
	               .contentType(jsonContentType))
					.andExpect(status().isOk())
	                .andExpect(jsonPath("$", hasSize(0)));
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1TagsTagIdGet_withValidId_shouldReturnTag() throws Exception {
		mockMvc.perform(get("/api/v1/tags/1")
	               .contentType(jsonContentType))
					.andExpect(status().isOk())
	                .andExpect(jsonPath("$.name", is("fancy")));
	}
	
	@Test
	public void testApiV1TagsTagIdGet_withNonExistingId_shouldReturnNotFound() throws Exception {
		mockMvc.perform(get("/api/v1/tags/3487634")
	               .contentType(jsonContentType))
				   .andExpect(status().isNotFound());
	}
	
	
	@Test
	public void testApiV1TagsTagIdGet_withInvalidId_shouldReturnBadRequest() throws Exception {
		mockMvc.perform(get("/api/v1/tags/totalywrong")
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1TagsTagIdDelete_withValidId_shouldDelete() throws Exception {
		mockMvc.perform(delete("/api/v1/tags/1")
	               .contentType(jsonContentType))
				   .andExpect(status().isOk());
		
		assertFalse(dao.exists(1l));
	}
	
	@Test
	public void testApiV1TagsTagIdDelete_withNonExistingId_shouldReturnNotFound() throws Exception {
		mockMvc.perform(delete("/api/v1/tags/9879797")
	               .contentType(jsonContentType))
				   .andExpect(status().isNotFound());
	}
	

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1TagsPost_withValidTag_shouldInsert() throws Exception {
		TagDTO tag = new TagDTO();
		tag.setName("groovy");
		
		mockMvc.perform(post("/api/v1/tags").content(json(tag))
	               .contentType(jsonContentType))
				   .andExpect(status().isCreated());
		
		TagDTO createdTag = service.getTag(4l);
		assertEquals("groovy",createdTag.getName());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1TagsPost_withAlreadyExistingName_shouldReturnConflict() throws Exception {
		TagDTO tag = new TagDTO();
		tag.setName("fancy");
		
		mockMvc.perform(post("/api/v1/tags").content(json(tag))
	               .contentType(jsonContentType))
				   .andExpect(status().isConflict());
	}
	
	@Test
	public void testApiV1TagsPost_withTagWithEmptyName_shouldReturnBadRequest() throws Exception {
		TagDTO tag = new TagDTO();
		tag.setName("");
		
		mockMvc.perform(post("/api/v1/tags").content(json(tag))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
		
	}
	
	@Test
	public void testApiV1TagsPost_withTagWithNullName_shouldReturnBadRequest() throws Exception {
		TagDTO tag = new TagDTO();
		tag.setName(null);
		
		mockMvc.perform(post("/api/v1/tags").content(json(tag))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
		
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1TagsPut_withValidTag_shouldUpdate() throws Exception {
		TagDTO tag = service.getTag(1l);
		tag.setName("groovy");
		
		mockMvc.perform(put("/api/v1/tags").content(json(tag))
	               .contentType(jsonContentType))
				   .andExpect(status().isOk());
		
		TagDTO createdTag = service.getTag(1l);
		assertEquals("groovy",createdTag.getName());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1TagsPut_withAlreadyExistingName_shouldReturnConflict() throws Exception {
		TagDTO tag = service.getTag(3l);
		tag.setName("fancy");
		
		mockMvc.perform(put("/api/v1/tags").content(json(tag))
	               .contentType(jsonContentType))
				   .andExpect(status().isConflict());
	}
	
	@Test
	public void testApiV1TagsPut_withNonExistingTag_shouldReturnNotFound() throws Exception {
		TagDTO tag = new TagDTO();
		tag.setTagId(435354l);
		tag.setName("groovy");
		
		mockMvc.perform(put("/api/v1/tags").content(json(tag))
	               .contentType(jsonContentType))
				   .andExpect(status().isNotFound());
	}
	
	
	@Test
	public void  testApiV1TagsPut_withTagWithEmptyName_shouldReturnBadRequest() throws Exception {
		TagDTO tag = new TagDTO();
		tag.setTagId(1l);
		tag.setName("");
		
		mockMvc.perform(post("/api/v1/tags").content(json(tag))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void  testApiV1TagsPut_withTagWithNullName_shouldReturnBadRequest() throws Exception {
		TagDTO tag = new TagDTO();
		tag.setTagId(1l);
		tag.setName(null);
		
		mockMvc.perform(post("/api/v1/tags").content(json(tag))
	               .contentType(jsonContentType))
				   .andExpect(status().isBadRequest());
	}
	
	
	@Test
	@FlywayTest(invokeCleanDB=true)
	public void testApiV1TagsRewindTagIdPut_withValidVersion_shouldRewind() throws Exception {
		Long id = service.createTag("guide").getTagId();
		
		TagDTO tagToUpdate = new TagDTO();
		tagToUpdate.setTagId(id);
		tagToUpdate.setName("fancy guide");
		service.updateTag(tagToUpdate);
		TagDTO updatedTag = service.getTag(id);
		assertEquals("fancy guide",updatedTag.getName());
		
		
		mockMvc.perform(put("/api/v1/tags/rewind/" + id)
	               .contentType(jsonContentType))
				   .andExpect(status().isOk());
		
		
		TagDTO rewindTag = service.getTag(id);
		assertEquals("guide",rewindTag.getName());
	}
	
	@Test
	@FlywayTest(invokeCleanDB=true)
	public void testApiV1TagsRewindTagIdPut_withNoPrevVersion_shouldReturnConflict() throws Exception {
		mockMvc.perform(put("/api/v1/tags/rewind/1")
	               .contentType(jsonContentType))
				   .andExpect(status().isConflict());
	}
}