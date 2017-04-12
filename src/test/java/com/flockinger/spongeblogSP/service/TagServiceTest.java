package com.flockinger.spongeblogSP.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.flockinger.spongeblogSP.dao.TagDAO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.TagPostsDTO;
import com.flockinger.spongeblogSP.dto.link.PostLink;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.model.Tag;
import com.flockinger.spongeblogSP.model.enums.PostStatus;

public class TagServiceTest extends BaseServiceTest {

	@Autowired
	private TagService service;
	
	@Autowired 
	private TagDAO dao;

	@Test
	@FlywayTest(invokeCleanDB = true)
	public void testGetAllTags__withEmptyDB_shouldReturnEmpty() {
		List<TagDTO> tags = service.getAllTags();

		assertNotNull(tags);
		assertTrue(tags.size() == 0);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetAllTags_shouldReturnAll() {
		List<TagDTO> tags = service.getAllTags();

		assertNotNull(tags);
		assertTrue(tags.size() == 3);
		assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("fancy")));
		assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("guide")));
		assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("cold")));
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetTag_withValidId_shouldReturnCorrectTag() throws EntityIsNotExistingException {
		TagPostsDTO tagPost = service.getTag(1l);

		assertNotNull(tagPost);
		assertEquals("fancy", tagPost.getName());
		List<PostLink> links = tagPost.getPosts();
		assertNotNull(links);
		assertTrue(links.size() == 2);
		assertTrue(links.stream().anyMatch(link -> link.getId() == 1));
		assertTrue(links.stream().anyMatch(link -> link.getId() == 2));
	}

	@Test(expected = EntityIsNotExistingException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetTag_withNotValidId_shouldReturnCorrectTag() throws EntityIsNotExistingException {
		service.getTag(176576l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testCreateTag_withValidName_shouldWork() throws DuplicateEntityException, EntityIsNotExistingException {
		String tagName = "ordinary";
		Long newTagId = service.createTag(tagName).getId();

		TagPostsDTO newTag = service.getTag(newTagId);

		assertNotNull(newTag);
		assertEquals("ordinary", newTag.getName());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdateTag_withValidName_shouldWork() throws DuplicateEntityException, EntityIsNotExistingException {

		TagDTO savedTag = new TagDTO();
		savedTag.setId(1l);
		savedTag.setName("new better name");
		service.updateTag(savedTag);

		TagPostsDTO updatedTag = service.getTag(1l);

		assertNotNull(updatedTag);
		assertEquals("new better name", updatedTag.getName());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testDeleteTag_withValidName_shouldWork() throws DuplicateEntityException, EntityIsNotExistingException {
		service.deleteTag(1l);

		assertFalse(dao.exists(1l));
	}

	@Test(expected = DuplicateEntityException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testCreateTag_withAlreadyExistingName_shouldThrowException() throws DuplicateEntityException {
		service.createTag("fancy");
	}

	@Test(expected = ConstraintViolationException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testCreateTag_withTooLongName_shouldThrowException()
			throws ConstraintViolationException, DuplicateEntityException {
		StringBuilder tooLoonTag = new StringBuilder();
		for(int i=0;i<151;i++){
			tooLoonTag.append("a");
		}
		
		service.createTag(tooLoonTag.toString());
	}

	@Test(expected = DataIntegrityViolationException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdateTag_withAlreadyExistingName_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException {
		TagDTO savedTag = new TagDTO();
		savedTag.setId(2l);
		savedTag.setName("guide");
		service.updateTag(savedTag);
	}

	@Test(expected = EntityIsNotExistingException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testDeleteTag_withNotValidId_shouldDeleteTag() throws EntityIsNotExistingException {
		service.deleteTag(23434234l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testInnerSortPostsByDateDescending_withManyPosts_shouldSortByDateDescending() throws Exception {
		Tag tag = new Tag();
		Date now = new Date();
		List<Post> posts = new LinkedList<>();

		Post post1 = new Post();
		post1.setTitle("first post newest");
		post1.setCreated(now);

		Post post2 = new Post();
		post2.setTitle("second post almost new");
		post2.setCreated(DateUtils.addSeconds(now, -1));

		Post post3 = new Post();
		post3.setTitle("third post used");
		post3.setCreated(DateUtils.addHours(now, -1));

		Post post4 = new Post();
		post4.setTitle("fourth post more used");
		post4.setCreated(DateUtils.addDays(now, -5));

		Post post5 = new Post();
		post5.setTitle("fifthpost old");
		post5.setCreated(DateUtils.addMonths(now, -2));

		Post post6 = new Post();
		post6.setTitle("sixth post oldest");
		post6.setCreated(DateUtils.addYears(now, -1));

		posts.add(post5);
		posts.add(post6);
		posts.add(post4);
		posts.add(post2);
		posts.add(post1);
		posts.add(post3);

		Collections.shuffle(posts); // mix it real good

		tag.setPosts(posts);
		TagServiceImpl realService = new TagServiceImpl();

		Tag sortedTag = Whitebox.<Tag>invokeMethod(realService, "sortPostsByDateDescending", tag);
		assertNotNull(sortedTag);
		List<Post> sortedPosts = sortedTag.getPosts();

		assertThat(sortedPosts.get(0).getTitle()).isEqualTo("first post newest");
		assertThat(sortedPosts.get(1).getTitle()).isEqualTo("second post almost new");
		assertThat(sortedPosts.get(2).getTitle()).isEqualTo("third post used");
		assertThat(sortedPosts.get(3).getTitle()).isEqualTo("fourth post more used");
		assertThat(sortedPosts.get(4).getTitle()).isEqualTo("fifthpost old");
		assertThat(sortedPosts.get(5).getTitle()).isEqualTo("sixth post oldest");
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testInnerSortPostsByDateDescending_withNoPosts_shouldDoNothing() throws Exception {
		Tag tag = new Tag();

		TagServiceImpl realService = new TagServiceImpl();

		Whitebox.<Tag>invokeMethod(realService, "sortPostsByDateDescending", tag);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testInnerSortPostsByDateDescending_withPostsWithoutDates_shouldDoNothing() throws Exception {
		Tag tag = new Tag();
		List<Post> posts = new LinkedList<>();

		Post post1 = new Post();
		post1.setTitle("first post newest");

		Post post2 = new Post();
		post2.setTitle("second post almost new");

		Post post3 = new Post();
		post3.setTitle("third post used");

		Post post4 = new Post();
		post4.setTitle("fourth post more used");

		posts.add(post4);
		posts.add(post2);
		posts.add(post1);
		posts.add(post3);

		tag.setPosts(posts);
		TagServiceImpl realService = new TagServiceImpl();

		Whitebox.<Tag>invokeMethod(realService, "sortPostsByDateDescending", tag);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testInnerSortPostsByDateDescending_withSomePostsWithoutDates_shouldSortCorrectlyAndNullsLast()
			throws Exception {
		Tag tag = new Tag();
		Date now = new Date();
		List<Post> posts = new LinkedList<>();

		Post post1 = new Post();
		post1.setTitle("first post newest");
		post1.setCreated(now);

		Post post2 = new Post();
		post2.setTitle("invalid withoutDate");

		Post post3 = new Post();
		post3.setTitle("third post used");
		post3.setCreated(DateUtils.addHours(now, -1));

		Post post4 = new Post();
		post4.setTitle("another without date");

		posts.add(post4);
		posts.add(post2);
		posts.add(post1);
		posts.add(post3);

		tag.setPosts(posts);
		TagServiceImpl realService = new TagServiceImpl();

		Tag sortedTag = Whitebox.<Tag>invokeMethod(realService, "sortPostsByDateDescending", tag);
		assertNotNull(sortedTag);
		List<Post> sortedPosts = sortedTag.getPosts();

		assertThat(sortedPosts.get(0).getTitle()).isEqualTo("first post newest");
		assertThat(sortedPosts.get(1).getTitle()).isEqualTo("third post used");
		assertThat(sortedPosts.get(2).getTitle()).isIn("invalid withoutDate", "another without date");
		assertThat(sortedPosts.get(3).getTitle()).isIn("invalid withoutDate", "another without date");
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testInnerFilterNonPublicPosts_withSomeNonPublicPosts_shouldFilter() throws Exception {
		Tag tag = new Tag();
		List<Post> posts = new LinkedList<>();

		Post post1 = new Post();
		post1.setTitle("first post newest");
		post1.setStatus(PostStatus.PUBLIC);

		Post post2 = new Post();
		post2.setTitle("invalid withoutDate");
		post2.setStatus(PostStatus.PRIVATE);

		Post post3 = new Post();
		post3.setTitle("third post used");
		post3.setStatus(PostStatus.MAINTENANCE);

		Post post4 = new Post();
		post4.setTitle("another without date");
		post4.setStatus(PostStatus.PUBLIC);

		Post post5 = new Post();
		post5.setTitle("another without date");
		post5.setStatus(PostStatus.PRIVATE);

		posts.add(post1);
		posts.add(post2);
		posts.add(post3);
		posts.add(post4);
		posts.add(post5);
		tag.setPosts(posts);

		TagServiceImpl realService = new TagServiceImpl();

		Tag filteredTag = Whitebox.<Tag>invokeMethod(realService, "filterNonPublicPosts", tag);
		assertNotNull(filteredTag);
		assertThat(filteredTag.getPosts()).asList().containsExactlyInAnyOrder(post1, post4);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testInnerFilterNonPublicPosts_withNoPosts_shouldDoNothing() throws Exception {
		TagServiceImpl realService = new TagServiceImpl();

		Whitebox.<Tag>invokeMethod(realService, "filterNonPublicPosts", new Tag());
	}
}
