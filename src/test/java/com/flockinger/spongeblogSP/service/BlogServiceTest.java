package com.flockinger.spongeblogSP.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import com.flockinger.spongeblogSP.dto.BlogDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.model.enums.BlogStatus;
import com.google.common.collect.ImmutableMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_CLASS)
public class BlogServiceTest {
	
	@Autowired
	private BlogService service;
	
	
	public void testGetBlog_shouldReturnBlog() throws EntityIsNotExistingException{
		BlogDTO blog = service.getBlog();
		
		assertNotNull(blog);
		assertEquals("test blog", blog.getName());
		assertEquals(BlogStatus.ACTIVE, blog.getStatus());
		
		assertNotNull(blog.getSettings());
		Map<String,String> settings = blog.getSettings();
		assertEquals("all-black", settings.get("blog theme"));
		assertEquals("hide", settings.get("footer"));
	}
	
	public void testDeleteCreateUpdate_withValidBlog_shouldWork() throws EntityIsNotExistingException, DuplicateEntityException {
		//delete
		service.deleteBlog(1l);
		
		//create
		BlogDTO newBlog = new BlogDTO();
		newBlog.setName("Spacy blog");
		newBlog.setStatus(BlogStatus.DISABLED);
		newBlog.setSettings(ImmutableMap.of("text-color","white","background-image","bunny.jpg"));
		service.createBlog(newBlog);
		
		//verify created
		BlogDTO savedBlog = service.getBlog();
		assertThat(savedBlog.getName()).isEqualTo("Spacy blog");
		assertThat(savedBlog.getStatus()).isEqualTo(BlogStatus.DISABLED);
		assertThat(savedBlog.getSettings()).containsAllEntriesOf(ImmutableMap.of("text-color","white","background-image","bunny.jpg"));
		
		//update
		savedBlog.setName("Hyper-Space blog");
		savedBlog.setStatus(BlogStatus.ACTIVE);
		savedBlog.setSettings(ImmutableMap.of("text-color","black","background-image","enterprise.jpg"));
		service.updateBlog(savedBlog);
		
		//verify updated
		BlogDTO updatedBlog = service.getBlog();
		assertThat(updatedBlog.getName()).isEqualTo("Hyper-Space blog");
		assertThat(updatedBlog.getStatus()).isEqualTo(BlogStatus.ACTIVE);
		assertThat(updatedBlog.getSettings()).containsAllEntriesOf(ImmutableMap.of("text-color","black","background-image","enterprise.jpg"));
	}
	
	@Test(expected=DuplicateEntityException.class)
	public void test_createBlog_whenItalreadyExists_shouldThrowException() throws DuplicateEntityException{
		BlogDTO hackBlog = new BlogDTO();
		hackBlog.setName("Spacy blog");
		hackBlog.setStatus(BlogStatus.DISABLED);
		hackBlog.setSettings(ImmutableMap.of("text-color","white","background-image","bunny.jpg"));
		
		service.createBlog(hackBlog);
	}
	
	@Test(expected=EntityIsNotExistingException.class)
	public void testUpdateBlog_withNotExistingId_shouldThrowException() throws EntityIsNotExistingException {
		BlogDTO fakeBlog = new BlogDTO();
		fakeBlog.setId(34l);
		fakeBlog.setName("Spacy blog");
		fakeBlog.setStatus(BlogStatus.DISABLED);
		fakeBlog.setSettings(ImmutableMap.of("text-color","white","background-image","bunny.jpg"));
		
		service.updateBlog(fakeBlog);
	}
	
	@Test(expected=TransactionSystemException.class)
	public void testUpdateBlog_withEmptyName_shouldThrowException() throws EntityIsNotExistingException {
		BlogDTO fakeBlog = new BlogDTO();
		fakeBlog.setId(1l);
		fakeBlog.setStatus(BlogStatus.DISABLED);
		fakeBlog.setSettings(ImmutableMap.of("text-color","white","background-image","bunny.jpg"));
		
		service.updateBlog(fakeBlog);
	}
	
	@Test(expected=TransactionSystemException.class)
	public void testUpdateBlog_withMissingStatus_shouldThrowException() throws EntityIsNotExistingException {
		BlogDTO fakeBlog = new BlogDTO();
		fakeBlog.setId(1l);
		fakeBlog.setName("Free Blog");
		fakeBlog.setSettings(ImmutableMap.of("text-color","white","background-image","bunny.jpg"));
		
		service.updateBlog(fakeBlog);
	}
	
	
	@Test(expected=EntityIsNotExistingException.class)
	public void test_deleteBlog_withNotExistingId_shouldThrowException() throws EntityIsNotExistingException{
		service.deleteBlog(3423l);
	}
}
