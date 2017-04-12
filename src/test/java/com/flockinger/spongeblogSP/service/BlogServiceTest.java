package com.flockinger.spongeblogSP.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;

import com.flockinger.spongeblogSP.dao.BlogDAO;
import com.flockinger.spongeblogSP.dto.BlogDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.model.enums.BlogStatus;
import com.google.common.collect.ImmutableMap;

public class BlogServiceTest extends BaseServiceTest {

	@Autowired
	private BlogService service;
	
	@Autowired
	private BlogDAO dao;

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetBlog_shouldReturnBlog() throws EntityIsNotExistingException {
		BlogDTO blog = service.getBlog();

		assertNotNull(blog);
		assertEquals("test blog", blog.getName());
		assertEquals(BlogStatus.ACTIVE, blog.getStatus());

		assertNotNull(blog.getSettings());
		Map<String, String> settings = blog.getSettings();
		assertEquals("all-black", settings.get("blog theme"));
		assertEquals("hide", settings.get("footer"));
	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testCreate_withValidBlog_shouldWork() throws EntityIsNotExistingException, DuplicateEntityException {
		BlogDTO newBlog = new BlogDTO();
		newBlog.setName("Spacy blog");
		newBlog.setStatus(BlogStatus.DISABLED);
		newBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));
		service.createBlog(newBlog);

		
		BlogDTO savedBlog = service.getBlog();
		assertThat(savedBlog.getName()).isEqualTo("Spacy blog");
		assertThat(savedBlog.getStatus()).isEqualTo(BlogStatus.DISABLED);
		assertThat(savedBlog.getSettings())
				.containsAllEntriesOf(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdate_withValidBlog_shouldWork() throws EntityIsNotExistingException, DuplicateEntityException {
		BlogDTO savedBlog = service.getBlog();

		savedBlog.setName("Hyper-Space blog");
		savedBlog.setStatus(BlogStatus.ACTIVE);
		savedBlog.setSettings(ImmutableMap.of("text-color", "black", "background-image", "enterprise.jpg"));
		service.updateBlog(savedBlog);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testDelete_withValidBlog_shouldWork()
			throws EntityIsNotExistingException, DuplicateEntityException {
		Long blogId = service.getBlog().getId();
		service.deleteBlog(blogId);

		assertNull(dao.findOne(blogId));
	}

	@Test(expected = DuplicateEntityException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void test_createBlog_whenItalreadyExists_shouldThrowException() throws DuplicateEntityException {
		BlogDTO hackBlog = new BlogDTO();
		hackBlog.setName("Spacy blog");
		hackBlog.setStatus(BlogStatus.DISABLED);
		hackBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));

		service.createBlog(hackBlog);
	}

	@Test(expected = EntityIsNotExistingException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdateBlog_withNotExistingId_shouldThrowException() throws EntityIsNotExistingException {
		BlogDTO fakeBlog = new BlogDTO();
		fakeBlog.setId(34l);
		fakeBlog.setName("Spacy blog");
		fakeBlog.setStatus(BlogStatus.DISABLED);
		fakeBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));

		service.updateBlog(fakeBlog);
	}

	@Test(expected = TransactionSystemException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdateBlog_withEmptyName_shouldThrowException() throws EntityIsNotExistingException {
		BlogDTO fakeBlog = new BlogDTO();
		fakeBlog.setId(1l);
		fakeBlog.setStatus(BlogStatus.DISABLED);
		fakeBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));

		service.updateBlog(fakeBlog);
	}

	@Test(expected = TransactionSystemException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdateBlog_withMissingStatus_shouldThrowException() throws EntityIsNotExistingException {
		BlogDTO fakeBlog = new BlogDTO();
		fakeBlog.setId(1l);
		fakeBlog.setName("Free Blog");
		fakeBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));

		service.updateBlog(fakeBlog);
	}

	@Test(expected = EntityIsNotExistingException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void test_deleteBlog_withNotExistingId_shouldThrowException() throws EntityIsNotExistingException {
		service.deleteBlog(3423l);
	}
}
