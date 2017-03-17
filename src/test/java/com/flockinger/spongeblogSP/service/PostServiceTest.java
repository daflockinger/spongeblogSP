package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.dto.link.PostLink;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.model.enums.PostStatus;
import com.google.common.collect.ImmutableList;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class PostServiceTest {

	@Autowired
	private PostService service;

	@Test
	public void testGetAllPosts_withPaging_shouldReturnPageContent() {
		Pageable secondPageDateDescending = createPage(1, 3, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getAllPosts(secondPageDateDescending);

		assertNotNull(posts);
		assertTrue(posts.get(0).getId() == 6l);
		assertTrue(posts.get(1).getId() == 5l);
		assertTrue(posts.get(2).getId() == 4l);
	}

	@Test
	public void testGetAllPostWithStatus_withValidStatus_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getAllPostsWithStatus(PostStatus.PUBLIC, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getId() == 1l);
		assertTrue(posts.get(1).getId() == 2l);
		assertTrue(posts.get(2).getId() == 3l);
		assertTrue(posts.get(3).getId() == 6l);
		assertTrue(posts.get(4).getId() == 5l);
	}

	@Test
	public void testGetAllPostFromCategoryId_withValidCategory_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getPostsFromCategoryId(1l, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getId() == 1l);
		assertTrue(posts.get(1).getId() == 3l);
		assertTrue(posts.get(2).getId() == 5l);
	}

	@Test
	public void testGetAllPostFromCategoryIdAndStatus_withValidCategoryAndStatus_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getPostsFromCategoryIdWithStatus(2l, PostStatus.PUBLIC, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getId() == 2l);
		assertTrue(posts.get(1).getId() == 6l);
	}

	@Test
	public void testGetAllPostFromAuthor_withValidAuthor_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getPostsFromAuthorId(1l, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getId() == 1l);
		assertTrue(posts.get(1).getId() == 2l);
		assertTrue(posts.get(2).getId() == 3l);
		assertTrue(posts.get(3).getId() == 6l);
		assertTrue(posts.get(4).getId() == 5l);
	}

	@Test
	public void testGetAllPostFromAuthorAndStatus_withValidAuthorAndStatus_shouldReturnFiltered() {
		Pageable firstPageDateDescending = createPage(0, 6, createSort(Direction.DESC, "created"));

		List<PostLink> posts = service.getPostsFromAuthorIdWithStatus(1l, PostStatus.DELETED, firstPageDateDescending);
		assertNotNull(posts);
		assertTrue(posts.get(0).getId() == 4l);
	}

	@Test
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
	public void testCreateUpdateDeletePost_withValidPost_shouldCreateAndDeleteThenPost()
			throws DuplicateEntityException, EntityIsNotExistingException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate);
		freshPost.setModified(freshDate);
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getId();

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
		assertNotNull(savedPost.getCreated().getTime());
		assertNotNull(savedPost.getModified().getTime());
		assertEquals(PostStatus.PUBLIC, savedPost.getStatus());
		assertEquals("Fresh out of the box", savedPost.getTitle());
		// verifying tags
		assertNotNull(savedPost.getTags());
		assertTrue(savedPost.getTags().size() == 1);
		assertEquals("fancy", savedPost.getTags().stream().findFirst().get().getName());

		// update post
		savedPost.setAuthor(getTestUser(2l));
		savedPost.setCategory(getTestCategory(1l));
		savedPost.setContent("Some updated new content...");
		savedPost.setCreated(freshDate);
		savedPost.setModified(freshDate);
		savedPost.setStatus(PostStatus.MAINTENANCE);
		savedPost.setTitle("Updated out of the box");
		savedPost.setTags(ImmutableList.of(getTag(1l), getTag(2l)));
		service.updatePost(savedPost);

		PostDTO updatedPost = service.getPost(savedPost.getId());

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
		assertNotNull(updatedPost.getCreated().getTime());
		assertNotNull(updatedPost.getModified().getTime());
		assertEquals(PostStatus.MAINTENANCE, updatedPost.getStatus());
		assertEquals("Updated out of the box", updatedPost.getTitle());
		// verifying tags
		assertNotNull(updatedPost.getTags());
		assertTrue(updatedPost.getTags().size() == 2);
		assertTrue(updatedPost.getTags().stream().anyMatch(tag -> tag.getName().equals("fancy")));
		assertTrue(updatedPost.getTags().stream().anyMatch(tag -> tag.getName().equals("cold")));

		// remove new post
		service.deletePost(updatedPost);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testCreatePost_withPostWithInvalidNonExistingUserOnCreate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(123l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate);
		freshPost.setModified(freshDate);
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getId();

		PostDTO savedPost = service.getPost(freshId);

		service.deletePost(savedPost);
	}

	@Test(expected = JpaObjectRetrievalFailureException.class)
	public void testUpdatePost_withPostWithInvalidNonExistingUserOnUpdate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate);
		freshPost.setModified(freshDate);
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getId();

		PostDTO savedPost = service.getPost(freshId);

		// update post
		savedPost.setAuthor(getTestUser(232l));
		savedPost.setCategory(getTestCategory(1l));
		savedPost.setContent("Some updated new content...");
		savedPost.setCreated(freshDate);
		savedPost.setModified(freshDate);
		savedPost.setStatus(PostStatus.MAINTENANCE);
		savedPost.setTitle("Updated out of the box");
		savedPost.setTags(ImmutableList.of(getTag(1l), getTag(2l)));

		try {
			service.updatePost(savedPost);
		} catch (JpaObjectRetrievalFailureException exception) {
			throw exception;
		} finally {
			// remove new post
			service.deletePost(savedPost);
		}
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testCreatePost_withPostWithInvalidNonExistingCategoryOnCreate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(254l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate);
		freshPost.setModified(freshDate);
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getId();

		PostDTO savedPost = service.getPost(freshId);

		service.deletePost(savedPost);
	}

	@Test(expected = JpaObjectRetrievalFailureException.class)
	public void testUpdatePost_withPostWithInvalidNonExistingCategoryOnUpdate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate);
		freshPost.setModified(freshDate);
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getId();

		PostDTO savedPost = service.getPost(freshId);

		// update post
		savedPost.setAuthor(getTestUser(2l));
		savedPost.setCategory(getTestCategory(156l));
		savedPost.setContent("Some updated new content...");
		savedPost.setCreated(freshDate);
		savedPost.setModified(freshDate);
		savedPost.setStatus(PostStatus.MAINTENANCE);
		savedPost.setTitle("Updated out of the box");
		savedPost.setTags(ImmutableList.of(getTag(1l), getTag(2l)));

		try {
			service.updatePost(savedPost);
		} catch (JpaObjectRetrievalFailureException exception) {
			throw exception;
		} finally {
			// remove new post
			service.deletePost(savedPost);
		}
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testCreatePost_withPostWithInvalidNonExistingTagOnCreate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate);
		freshPost.setModified(freshDate);
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(154l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getId();

		PostDTO savedPost = service.getPost(freshId);

		service.deletePost(savedPost);
	}

	@Test(expected = JpaObjectRetrievalFailureException.class)
	public void testUpdatePost_withPostWithInvalidNonExistingTagOnUpdate_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate);
		freshPost.setModified(freshDate);
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getId();

		PostDTO savedPost = service.getPost(freshId);

		// update post
		savedPost.setAuthor(getTestUser(2l));
		savedPost.setCategory(getTestCategory(1l));
		savedPost.setContent("Some updated new content...");
		savedPost.setCreated(freshDate);
		savedPost.setModified(freshDate);
		savedPost.setStatus(PostStatus.MAINTENANCE);
		savedPost.setTitle("Updated out of the box");
		savedPost.setTags(ImmutableList.of(getTag(1345l), getTag(2l)));

		try {
			service.updatePost(savedPost);
		} catch (JpaObjectRetrievalFailureException exception) {
			throw exception;
		} finally {
			// remove new post
			service.deletePost(savedPost);
		}
	}

	@Test(expected = DuplicateEntityException.class)
	public void testCreatePost_withPostWithAlreadyExistingTitle_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate);
		freshPost.setModified(freshDate);
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("but");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getId();

		PostDTO savedPost = service.getPost(freshId);

		service.deletePost(savedPost);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testUpdatePost_withPostWithAlreadyExistingTitle_shouldThrowException()
			throws DuplicateEntityException, EntityIsNotExistingException {
		Date freshDate = new Date();

		PostDTO freshPost = new PostDTO();
		freshPost.setAuthor(getTestUser(1l));
		freshPost.setCategory(getTestCategory(2l));
		freshPost.setContent("Some fresh new content...");
		freshPost.setCreated(freshDate);
		freshPost.setModified(freshDate);
		freshPost.setStatus(PostStatus.PUBLIC);
		freshPost.setTitle("Fresh out of the box");
		freshPost.setTags(ImmutableList.of(getTag(1l)));

		// create new Post
		Long freshId = service.createPost(freshPost).getId();

		PostDTO savedPost = service.getPost(freshId);

		// update post
		savedPost.setAuthor(getTestUser(2l));
		savedPost.setCategory(getTestCategory(1l));
		savedPost.setContent("Some fresh new content...");
		savedPost.setCreated(freshDate);
		savedPost.setModified(freshDate);
		savedPost.setStatus(PostStatus.MAINTENANCE);
		savedPost.setTitle("but");
		savedPost.setTags(ImmutableList.of(getTag(1l), getTag(2l)));

		try {
			service.updatePost(savedPost);
		} catch (DataIntegrityViolationException exception) {
			throw exception;
		} finally {
			// remove new post
			service.deletePost(savedPost);
		}
	}

	@Test(expected = EntityIsNotExistingException.class)
	public void testDelete_withNonExitingId_shouldThrowException() throws EntityIsNotExistingException {
		PostDTO post = new PostDTO();
		post.setId(3423543l);
		service.deletePost(post);
	}

	private TagDTO getTag(Long id) {
		TagDTO tag = new TagDTO();
		tag.setId(id);

		return tag;
	}

	private CategoryDTO getTestCategory(Long id) {
		CategoryDTO cat = new CategoryDTO();
		cat.setId(id);
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
