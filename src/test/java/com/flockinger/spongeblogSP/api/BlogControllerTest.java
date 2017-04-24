package com.flockinger.spongeblogSP.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.spongeblogSP.dao.BlogDAO;
import com.flockinger.spongeblogSP.dto.BlogDTO;
import com.flockinger.spongeblogSP.model.enums.BlogStatus;
import com.flockinger.spongeblogSP.service.BlogService;
import com.google.common.collect.ImmutableMap;



public class BlogControllerTest extends BaseControllerTest {

	@Autowired
	private BlogDAO dao;
	
	@Autowired
	private BlogService service;
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1BlogGet_withExistingBlog_shouldReturnCorrect() throws Exception{
		mockMvc.perform(get("/api/v1/blog")
               .contentType(jsonContentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("test blog")))
                .andExpect(jsonPath("$.status", is(BlogStatus.ACTIVE.toString())))
                .andExpect(jsonPath("$.settings['blog theme']", is("all-black")))
                .andExpect(jsonPath("$.settings.footer", is("hide")))
                .andExpect(jsonPath("$._links.self",anything("blog")));
	}
	
	@Test
	@FlywayTest(invokeCleanDB=true)
	public void testApiV1BlogGet_withNoneExistingBlog_shouldReturnNotFound() throws Exception{
		mockMvc.perform(get("/api/v1/blog")
               .contentType(jsonContentType))
                .andExpect(status().isNotFound());
	}
	
	@Test
	@FlywayTest(invokeCleanDB=true)
	public void testApiV1BlogPost_withValidBlog_shouldWork() throws Exception{
		assertFalse(dao.findAll().iterator().hasNext());
		BlogDTO newBlog = new BlogDTO();
		newBlog.setName("Spacy blog");
		newBlog.setStatus(BlogStatus.DISABLED);
		newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));
		
		mockMvc.perform(post("/api/v1/blog")
				.content(json(newBlog))
	            .contentType(jsonContentType))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.name", is("Spacy blog")))
                .andExpect(jsonPath("$.status", is(BlogStatus.DISABLED.toString())))
                .andExpect(jsonPath("$.settings['text-color']", is("white")))
                .andExpect(jsonPath("$.settings.['background-image']", is("bunny.jpg")))
                .andExpect(jsonPath("$._links.self",anything("blog")));
		
		assertTrue(dao.findAll().iterator().hasNext());
	}
	
	@Test
	public void testApiV1BlogPost_withInvalidName_shouldReturnBadRequest() throws Exception{
		BlogDTO newBlog = new BlogDTO();
		newBlog.setName("");
		newBlog.setStatus(BlogStatus.DISABLED);
		newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));
		
		mockMvc.perform(post("/api/v1/blog")
				.content(json(newBlog))
	            .contentType(jsonContentType))
	            .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1BlogPost_withNullName_shouldReturnBadRequest() throws Exception{
		BlogDTO newBlog = new BlogDTO();
		newBlog.setName(null);
		newBlog.setStatus(BlogStatus.DISABLED);
		newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));
		
		mockMvc.perform(post("/api/v1/blog")
				.content(json(newBlog))
	            .contentType(jsonContentType))
	            .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1BlogPost_withNullStatus_shouldReturnBadRequest() throws Exception{
		BlogDTO newBlog = new BlogDTO();
		newBlog.setName("non status blog");
		newBlog.setStatus(null);
		newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));
		
		mockMvc.perform(post("/api/v1/blog")
				.content(json(newBlog))
	            .contentType(jsonContentType))
	            .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1BlogPost_withNullSettings_shouldReturnBadRequest() throws Exception{
		BlogDTO newBlog = new BlogDTO();
		newBlog.setName("non status blog");
		newBlog.setStatus(null);
		newBlog.setSettings(null);
		
		mockMvc.perform(post("/api/v1/blog")
				.content(json(newBlog))
	            .contentType(jsonContentType))
	            .andExpect(status().isBadRequest());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1BlogPost_withAlreadyExistingBlog_shouldRetunConflict() throws Exception{
		BlogDTO newBlog = new BlogDTO();
		newBlog.setName("Spacy blog");
		newBlog.setStatus(BlogStatus.DISABLED);
		newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));
		
		mockMvc.perform(post("/api/v1/blog")
				.content(json(newBlog))
	            .contentType(jsonContentType))
	            .andExpect(status().isConflict());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1BlogPut_withValidUpdate_shouldUpdate() throws Exception{
		BlogDTO savedBlog = service.getBlog();

		savedBlog.setName("Hyper-Space blog");
		savedBlog.setStatus(BlogStatus.ACTIVE);
		savedBlog.setSettings(ImmutableMap.of("text-color", "black", "background-image", "enterprise.jpg"));
		
		mockMvc.perform(put("/api/v1/blog")
				.content(json(savedBlog))
	            .contentType(jsonContentType))
	            .andExpect(status().isOk());
		
		BlogDTO updatedBlog = service.getBlog();
		assertEquals("Hyper-Space blog",updatedBlog.getName());
		assertEquals(BlogStatus.ACTIVE,updatedBlog.getStatus());
		assertThat(savedBlog.getSettings())
		.containsAllEntriesOf(ImmutableMap.of("text-color", "black", "background-image", "enterprise.jpg"));
	}
	
	@Test
	public void testApiV1BlogPut_withInvalidName_shouldReturnBadRequest() throws Exception{
		BlogDTO savedBlog = new BlogDTO();

		savedBlog.setName("" );
		savedBlog.setStatus(BlogStatus.ACTIVE);
		savedBlog.setSettings(ImmutableMap.of("text-color", "black", "background-image", "enterprise.jpg"));
		
		mockMvc.perform(put("/api/v1/blog")
				.content(json(savedBlog))
	            .contentType(jsonContentType))
	            .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testApiV1BlogPut_withInvalidStatus_shouldReturnBadRequest() throws Exception{
		BlogDTO savedBlog = new BlogDTO();

		savedBlog.setName("Space" );
		savedBlog.setStatus(null);
		savedBlog.setSettings(ImmutableMap.of("text-color", "black", "background-image", "enterprise.jpg"));
		
		mockMvc.perform(put("/api/v1/blog")
				.content(json(savedBlog))
	            .contentType(jsonContentType))
	            .andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void testApiV1BlogPut_withInvalidSettings_shouldReturnBadRequest() throws Exception{
		BlogDTO savedBlog = new BlogDTO();

		savedBlog.setName("Space" );
		savedBlog.setStatus(BlogStatus.ACTIVE);
		savedBlog.setSettings(null);
		
		mockMvc.perform(put("/api/v1/blog")
				.content(json(savedBlog))
	            .contentType(jsonContentType))
	            .andExpect(status().isBadRequest());
	}
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1BlogDelete_withExistingBlog_shouldDelete() throws Exception{
		mockMvc.perform(delete("/api/v1/blog")
	            .contentType(jsonContentType))
	            .andExpect(status().isOk());
	}
	
	@Test
	@FlywayTest(invokeCleanDB=true)
	public void testApiV1BlogDelete_withNonExistingBlog_shouldReturnNotFound() throws Exception{
		mockMvc.perform(delete("/api/v1/blog")
	            .contentType(jsonContentType))
	            .andExpect(status().isNotFound());
	}
	
	@Test
	@FlywayTest(invokeCleanDB=true)
	public void testApiV1BlogRewindPut_withExistingVersions_shouldRewindToOldBlog() throws Exception{
		BlogDTO blog = new BlogDTO();
		blog.setName("new blog");
		blog.setStatus(BlogStatus.MAINTENANCE);
		blog.setSettings(ImmutableMap.of("color", "blue"));
		BlogDTO createdBlog = service.createBlog(blog);

		createdBlog.setName("fancy active Blog");
		createdBlog.setStatus(BlogStatus.ACTIVE);
		createdBlog.setSettings(ImmutableMap.of("color", "red"));
		service.updateBlog(createdBlog);
		
		BlogDTO updatedBlog = service.getBlog();
		assertEquals("fancy active Blog",updatedBlog.getName());
		assertEquals(BlogStatus.ACTIVE,updatedBlog.getStatus());
		assertEquals("red",updatedBlog.getSettings().get("color"));
		
		mockMvc.perform(put("/api/v1/blog/rewind")
	            .contentType(jsonContentType))
	            .andExpect(status().isOk());
		
		BlogDTO rewindedBlog = service.getBlog();
		assertEquals("new blog",rewindedBlog.getName());
		assertEquals(BlogStatus.MAINTENANCE,rewindedBlog.getStatus());
		assertEquals("blue",rewindedBlog.getSettings().get("color"));
	}
	

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testApiV1BlogRewindPut_withNoVersions_shouldReturnConflict() throws Exception{		
		mockMvc.perform(put("/api/v1/blog/rewind")
	            .contentType(jsonContentType))
	            .andExpect(status().isConflict());
	}
}
