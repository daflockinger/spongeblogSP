package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.dto.link.PostLink;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.enums.PostStatus;
import com.google.common.collect.ImmutableList;

public class PostServiceTest extends BaseServiceTest {

	@Autowired
	private PostService service;

	@Autowired
	private PostDAO dao;

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetAllPosts_withPaging_shouldReturnPageContent() {
		Pageable secondPageDateDescending = createPage(1, 3, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getAllPosts(secondPageDateDescending);

		assertNotNull(posts);
		assertTrue(posts.get(0).getPostId() == 6l);
		assertTrue(posts.get(1).getPostId() == 5l);
		assertTrue(posts.get(2).getPostId() == 4l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetAllPostWithStatus_withValidStatus_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getAllPostsWithStatus(PostStatus.PUBLIC, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getPostId() == 1l);
		assertTrue(posts.get(1).getPostId() == 2l);
		assertTrue(posts.get(2).getPostId() == 3l);
		assertTrue(posts.get(3).getPostId() == 6l);
		assertTrue(posts.get(4).getPostId() == 5l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetAllPostFromCategoryId_withValidCategory_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getPostsFromCategoryId(1l, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getPostId() == 1l);
		assertTrue(posts.get(1).getPostId() == 3l);
		assertTrue(posts.get(2).getPostId() == 5l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetAllPostFromCategoryIdAndStatus_withValidCategoryAndStatus_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getPostsFromCategoryIdWithStatus(2l, PostStatus.PUBLIC, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getPostId() == 2l);
		assertTrue(posts.get(1).getPostId() == 6l);
	}
	
	
	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetAllPostFromTagId_withValidTag_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getPostsFromTagId(1l, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getPostId() == 1l);
		assertTrue(posts.get(1).getPostId() == 2l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetAllPostFromTagIdAndStatus_withValidTagAndStatus_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getPostsFromTagIdWithStatus(1l, PostStatus.PUBLIC, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getPostId() == 1l);
		assertTrue(posts.get(1).getPostId() == 2l);
	}
	

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetAllPostFromAuthor_withValidAuthor_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getPostsFromAuthorId(1l, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getPostId() == 1l);
		assertTrue(posts.get(1).getPostId() == 2l);
		assertTrue(posts.get(2).getPostId() == 3l);
		assertTrue(posts.get(3).getPostId() == 6l);
		assertTrue(posts.get(4).getPostId() == 5l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetAllPostFromAuthorAndStatus_withValidAuthorAndStatus_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getPostsFromAuthorIdWithStatus(1l, PostStatus.DELETED, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getPostId() == 4l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testGetPost_withValidId_shouldReturnPost() throws EntityIsNotExistingException {
		PostDTO post = service.getPost(1l);

		assertNotNull(post);
		assertEquals("some content...", post.getContent());
		assertNotNull(post.getCreated());
		assertNotNull(post.getModified());
		assertEquals(PostStatus.PUBLIC, post.getStatus());
		assertEquals("somethings", post.getTitle());

		UserInfoDTO user = post.getAuthor();
		assertNotNull(user);
		assertEquals("flo@kinger.cc", user.getEmail());
		assertTrue(user.getId() == 1l);
		assertEquals("daflo", user.getNickName());
		assertNotNull(user.getRegistered());

		CategoryDTO category = post.getCategory();
		assertNotNull(category);
		assertEquals("main category", category.getName());
		assertTrue(category.getRank() == 1);

		List<TagDTO> tags = post.getTags();
		assertNotNull(tags);
		assertTrue(tags.size() == 2);
		assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("fancy")));
		assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("cold")));
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testCreatePost_withValidPost_shouldCreateAndDeleteThenPost()
			throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate.getTime());
		freshPost.setModified(freshDate.getTime());
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getPostId();

		PostDTO savedPost = service.getPost(freshId);

		assertNotNull(savedPost);
		// verifying author
		assertNotNull(savedPost.getAuthor());
		assertEquals("flo@kinger.cc", savedPost.getAuthor().getEmail());
		assertEquals("daflo", savedPost.getAuthor().getNickName());
		assertNotNull(savedPost.getAuthor().getRegistered());
		// verifying category
		assertNotNull(savedPost.getCategory());
		assertEquals("sub category", savedPost.getCategory().getName());
		assertTrue(savedPost.getCategory().getParentId() == 1l);
		// verifying values
		assertEquals("Some fresh new content...", savedPost.getContent());
		assertNotNull(savedPost.getCreated());
		assertNotNull(savedPost.getModified());
		assertEquals(PostStatus.PUBLIC, savedPost.getStatus());
		assertEquals("Fresh out of the box", savedPost.getTitle());
		// verifying tags
		assertNotNull(savedPost.getTags());
		assertTrue(savedPost.getTags().size() == 1);
		assertEquals("fancy", savedPost.getTags().stream().findFirst().get().getName());
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdatePost_withValidPost_shouldCreateAndDeleteThenPost()
			throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		Date freshDate = new Date();
		PostDTO savedPost = service.getPost(1l);
		savedPost.setAuthor(getTestUser(2l));
		savedPost.setCategory(getTestCategory(1l));
		savedPost.setContent("Some updated new content...");
		savedPost.setCreated(freshDate.getTime());
		savedPost.setModified(freshDate.getTime());
		savedPost.setStatus(PostStatus.MAINTENANCE);
		savedPost.setTitle("Updated out of the box");
		savedPost.setTags(ImmutableList.of(getTag(3l)));
		service.updatePost(savedPost);

		PostDTO updatedPost = service.getPost(savedPost.getPostId());

		assertNotNull(updatedPost);
		// verifying author
		assertNotNull(updatedPost.getAuthor());
		assertEquals("no@body.cc", updatedPost.getAuthor().getEmail());
		assertEquals("body", updatedPost.getAuthor().getNickName());
		assertNotNull(updatedPost.getAuthor().getRegistered());
		// verifying category
		assertNotNull(updatedPost.getCategory());
		assertEquals("main category", updatedPost.getCategory().getName());
		assertNull(updatedPost.getCategory().getParentId());
		// verifying values
		assertEquals("Some updated new content...", updatedPost.getContent());
		assertNotNull(updatedPost.getCreated());
		assertNotNull(updatedPost.getModified());
		assertEquals(PostStatus.MAINTENANCE, updatedPost.getStatus());
		assertEquals("Updated out of the box", updatedPost.getTitle());
		// verifying tags
		assertNotNull(updatedPost.getTags());
		assertTrue(updatedPost.getTags().size() == 1);
		assertTrue(updatedPost.getTags().stream().anyMatch(tag -> tag.getName().equals("guide")));
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testDeletePost_withValidPost_shouldCreateAndDeleteThenPost()
			throws DuplicateEntityException, EntityIsNotExistingException {
		service.deletePost(1l);
		assertFalse(dao.exists(1l));
	}

	@Test(expected = DependencyNotFoundException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testCreatePost_withPostWithInvalidNonExistingUserOnCreate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		Date freshDate = new Date();
		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(123l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate.getTime());
		freshPost.setModified(freshDate.getTime());
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		service.createPost(freshPost).getPostId();
		System.out.println();
	}

	@Test(expected = DependencyNotFoundException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdatePost_withPostWithInvalidNonExistingUserOnUpdate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		Date freshDate = new Date();
		PostDTO savedPost = service.getPost(1l);
		savedPost.setAuthor(getTestUser(232l));
		savedPost.setCategory(getTestCategory(1l));
		savedPost.setContent("Some updated new content...");
		savedPost.setCreated(freshDate.getTime());
		savedPost.setModified(freshDate.getTime());
		savedPost.setStatus(PostStatus.MAINTENANCE);
		savedPost.setTitle("Updated out of the box");
		savedPost.setTags(ImmutableList.of(getTag(1l), getTag(2l)));

		service.updatePost(savedPost);
	}

	@Test(expected = DependencyNotFoundException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testCreatePost_withPostWithInvalidNonExistingCategoryOnCreate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		Date freshDate = new Date();
		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(254l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate.getTime());
		freshPost.setModified(freshDate.getTime());
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		service.createPost(freshPost).getPostId();
	}

	@Test(expected = DependencyNotFoundException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdatePost_withPostWithInvalidNonExistingCategoryOnUpdate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		Date freshDate = new Date();
		PostDTO savedPost = service.getPost(1l);
		savedPost.setAuthor(getTestUser(2l));
		savedPost.setCategory(getTestCategory(156l));
		savedPost.setContent("Some updated new content...");
		savedPost.setCreated(freshDate.getTime());
		savedPost.setModified(freshDate.getTime());
		savedPost.setStatus(PostStatus.MAINTENANCE);
		savedPost.setTitle("Updated out of the box");
		savedPost.setTags(ImmutableList.of(getTag(1l), getTag(2l)));

		service.updatePost(savedPost);
	}

	@Test(expected = DependencyNotFoundException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testCreatePost_withPostWithInvalidNonExistingTagOnCreate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		Date freshDate = new Date();
		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate.getTime());
		freshPost.setModified(freshDate.getTime());
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(154l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getPostId();

		service.getPost(freshId);
	}

	@Test(expected = DependencyNotFoundException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdatePost_withPostWithInvalidNonExistingTagOnUpdate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		Date freshDate = new Date();
		PostDTO savedPost = service.getPost(1l);
		savedPost.setAuthor(getTestUser(2l));
		savedPost.setCategory(getTestCategory(1l));
		savedPost.setContent("Some updated new content...");
		savedPost.setCreated(freshDate.getTime());
		savedPost.setModified(freshDate.getTime());
		savedPost.setStatus(PostStatus.MAINTENANCE);
		savedPost.setTitle("Updated out of the box");
		savedPost.setTags(ImmutableList.of(getTag(1345l), getTag(2l)));

		service.updatePost(savedPost);
	}

	@Test(expected = DuplicateEntityException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testCreatePost_withPostWithAlreadyExistingTitle_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate.getTime());
		freshPost.setModified(freshDate.getTime());
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("somethings");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		service.createPost(freshPost).getPostId();
	}

	@Test(expected = DuplicateEntityException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testUpdatePost_withPostWithAlreadyExistingTitle_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		PostDTO savedPost = service.getPost(2l);
		savedPost.setTitle("somethings");

		service.updatePost(savedPost);
	}

	@Test(expected = EntityIsNotExistingException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testDelete_withNonExitingId_shouldThrowException() throws EntityIsNotExistingException {
		service.deletePost(3423543l);
	}

	@Test
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	@Transactional
	public void testRewind_withExistingPrevVersion_shouldRewind()
			throws NoVersionFoundException, DuplicateEntityException, EntityIsNotExistingException, DependencyNotFoundException {
		PostDTO freshPost = service.getPost(1l);
		freshPost.setCreated(new Date().getTime());
		freshPost.setTags(ImmutableList.of(getTag(1l)));
		service.updatePost(freshPost);

		Date freshDate = new Date();
		PostDTO savedPost = service.getPost(1l);
		savedPost.setAuthor(getTestUser(2l));
		savedPost.setCategory(getTestCategory(1l));
		savedPost.setContent("Some updated new content...");
		savedPost.setCreated(freshDate.getTime());
		savedPost.setModified(freshDate.getTime());
		savedPost.setStatus(PostStatus.MAINTENANCE);
		savedPost.setTitle("Updated out of the box");
		savedPost.setTags(ImmutableList.of(getTag(1l), getTag(3l)));
		service.updatePost(savedPost);

		PostDTO updatedPost = service.getPost(savedPost.getPostId());

		assertNotNull(updatedPost);
		// verifying author
		assertNotNull(updatedPost.getAuthor());
		assertEquals("no@body.cc", updatedPost.getAuthor().getEmail());
		assertEquals("body", updatedPost.getAuthor().getNickName());
		assertNotNull(updatedPost.getAuthor().getRegistered());
		// verifying category
		assertNotNull(updatedPost.getCategory());
		assertEquals("main category", updatedPost.getCategory().getName());
		assertNull(updatedPost.getCategory().getParentId());
		// verifying values
		assertEquals("Some updated new content...", updatedPost.getContent());
		assertNotNull(updatedPost.getCreated());
		assertNotNull(updatedPost.getModified());
		assertEquals(PostStatus.MAINTENANCE, updatedPost.getStatus());
		assertEquals("Updated out of the box", updatedPost.getTitle());
		// verifying tags
		assertNotNull(updatedPost.getTags());
		assertTrue(updatedPost.getTags().size() == 2);
		assertTrue(updatedPost.getTags().stream().anyMatch(tag -> tag.getName().equals("fancy")));
		assertTrue(updatedPost.getTags().stream().anyMatch(tag -> tag.getName().equals("guide")));

		service.rewind(savedPost.getPostId());

		PostDTO rewindPost = service.getPost(savedPost.getPostId());

		assertNotNull(rewindPost);
		// verifying author
		assertNotNull(rewindPost.getAuthor());
		assertEquals("flo@kinger.cc", rewindPost.getAuthor().getEmail());
		assertEquals("daflo", rewindPost.getAuthor().getNickName());
		assertNotNull(rewindPost.getAuthor().getRegistered());
		// verifying category
		assertNotNull(rewindPost.getCategory());
		assertEquals("main category", rewindPost.getCategory().getName());
		assertNull(rewindPost.getCategory().getParentId());
		// verifying values
		assertEquals("some content...", rewindPost.getContent());
		assertNotNull(rewindPost.getCreated());
		assertNotNull(rewindPost.getModified());
		assertEquals(PostStatus.PUBLIC, rewindPost.getStatus());
		assertEquals("somethings", rewindPost.getTitle());
		// verifying tags
		assertNotNull(rewindPost.getTags());
		assertTrue(rewindPost.getTags().size() == 1);
	}

	@Test(expected = NoVersionFoundException.class)
	@FlywayTest(locationsForMigrate = { "/db/testfill/" })
	public void testRewind_withNoPreviousVersion_shouldThrowException()
			throws NoVersionFoundException, DuplicateEntityException, EntityIsNotExistingException {
		service.rewind(1l);
	}

	private TagDTO getTag(Long id) {
		TagDTO tag = new TagDTO();
		tag.setId(id);

		return tag;
	}

	private CategoryDTO getTestCategory(Long id) {
		CategoryDTO cat = new CategoryDTO();
		cat.setCategoryId(id);
		return cat;
	}

	private UserInfoDTO getTestUser(Long id) {
		UserInfoDTO user = new UserInfoDTO();
		user.setId(id);
		return user;
	}

	private Sort createSort(Direction direction, String sortBy) {
		return new Sort(direction, sortBy);
	}

	private Pageable createPage(int pageNumber, int itemsPerPage, Sort sort) {
		return new Pageable() {

			@Override
			public Pageable previousOrFirst() {
				return null;
			}

			@Override
			public Pageable next() {
				return null;
			}

			@Override
			public boolean hasPrevious() {
				return false;
			}

			@Override
			public Sort getSort() {
				return sort;
			}

			@Override
			public int getPageSize() {
				return itemsPerPage;
			}

			@Override
			public int getPageNumber() {
				return pageNumber;
			}

			@Override
			public int getOffset() {
				return pageNumber * itemsPerPage;
			}

			@Override
			public Pageable first() {
				return null;
			}
		};
	}
}
