package com.flockinger.spongeblogSP.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.Map;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;

import com.flockinger.spongeblogSP.dao.BlogDAO;
import com.flockinger.spongeblogSP.dto.BlogDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
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
		
		BlogDTO updatedBlog = service.getBlog();
		assertEquals("Hyper-Space blog",updatedBlog.getName());
		assertEquals(BlogStatus.ACTIVE,updatedBlog.getStatus());
		assertThat(savedBlog.getSettings())
		.containsAllEntriesOf(ImmutableMap.of("text-color", "black", "background-image", "enterprise.jpg"));
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testDelete_withValidBlog_shouldWork() throws EntityIsNotExistingException, DuplicateEntityException {
		service.deleteBlog();

		assertFalse(dao.findAll().iterator().hasNext());
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

	@Test(expected = TransactionSystemException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdateBlog_withEmptyName_shouldThrowException() throws EntityIsNotExistingException {
		BlogDTO fakeBlog = new BlogDTO();
		fakeBlog.setStatus(BlogStatus.DISABLED);
		fakeBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));

		service.updateBlog(fakeBlog);
	}

	@Test(expected = TransactionSystemException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdateBlog_withMissingStatus_shouldThrowException() throws EntityIsNotExistingException {
		BlogDTO fakeBlog = new BlogDTO();
		fakeBlog.setName("Free Blog");
		fakeBlog.setSettings(ImmutableMap.of("text-color", "white", "background-image", "bunny.jpg"));

		service.updateBlog(fakeBlog);
	}

	@Test(expected = EntityIsNotExistingException.class)
	@FlywayTest(invokeCleanDB = true)
	public void test_deleteBlog_withNotExistingId_shouldThrowException() throws EntityIsNotExistingException {
		service.deleteBlog();
	}

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testRewind_withExistingPrevVersion_shouldRewind()
			throws NoVersionFoundException, DuplicateEntityException, EntityIsNotExistingException {
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
		
		service.rewind(null);
		BlogDTO rewindedBlog = service.getBlog();
		assertEquals("new blog",rewindedBlog.getName());
		assertEquals(BlogStatus.MAINTENANCE,rewindedBlog.getStatus());
		assertEquals("blue",rewindedBlog.getSettings().get("color"));
	}

	@Test(expected=NoVersionFoundException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testRewind_withNoPreviousVersion_shouldThrowException() throws NoVersionFoundException, DuplicateEntityException, EntityIsNotExistingException {
		service.rewind(null);
	}
}
