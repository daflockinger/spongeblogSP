package com.flockinger.spongeblogSP.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.TagPostsDTO;
import com.flockinger.spongeblogSP.dto.link.PostLink;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode=ClassMode.BEFORE_CLASS)
public class TagServiceTest {

	@Autowired
	private TagService service;
	
	@Test
	public void testGetAllTags_shouldReturnAll(){
		List<TagDTO> tags = service.getAllTags();
		
		assertNotNull(tags);
		assertTrue(tags.size() == 3);
		assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("fancy")));
		assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("guide")));
		assertTrue(tags.stream().anyMatch(tag -> tag.getName().equals("cold")));
	}
	
	@Test
	public void testGetTag_withValidId_shouldReturnCorrectTag() throws EntityIsNotExistingException{
		TagPostsDTO tagPost = service.getTag(1l);
		
		assertNotNull(tagPost);
		assertEquals("fancy", tagPost.getName());
		List<PostLink> links = tagPost.getPosts();
		assertNotNull(links);
		assertTrue(links.size() == 2);
		assertTrue(links.stream().anyMatch(link -> link.getId() == 1));
		assertTrue(links.stream().anyMatch(link -> link.getId() == 2));
	}
	
	@Test(expected=EntityIsNotExistingException.class)
	public void testGetTag_withNotValidId_shouldReturnCorrectTag() throws EntityIsNotExistingException{
		service.getTag(176576l);
	}
	
	@Test
	public void testCreateUpdateAndDeleteTag_withValidName_shouldWork() throws DuplicateEntityException, EntityIsNotExistingException{
		String tagName = "ordinary";
		Long newTagId = service.createTag(tagName).getId();
		
	    TagPostsDTO newTag = service.getTag(newTagId);
	    
	    assertNotNull(newTag);
	    assertEquals("ordinary", newTag.getName());
	    
	    
	    TagDTO savedTag = new TagDTO();
		savedTag.setId(newTagId);
		savedTag.setName("new better name");
		service.updateTag(savedTag);
		
	    TagPostsDTO updatedTag = service.getTag(newTagId);
	    
	    assertNotNull(updatedTag);
	    assertEquals("new better name", updatedTag.getName());
	    
	    service.deleteTag(newTagId);
		
		assertTrue(service.getAllTags().size() == 3);
	}
	
	@Test(expected=DuplicateEntityException.class)
	public void testCreateTag_withAlreadyExistingName_shouldThrowException() throws DuplicateEntityException{
		service.createTag("fancy");
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void testCreateTag_withTooLongName_shouldThrowException() throws ConstraintViolationException, DuplicateEntityException{
		service.createTag(RandomStringUtils.random(151));
	}

	@Test(expected=DataIntegrityViolationException.class)
	public void testUpdateTag_withAlreadyExistingName_shouldThrowException() throws DuplicateEntityException, EntityIsNotExistingException{
		TagDTO savedTag = new TagDTO();
		savedTag.setId(2l);
		savedTag.setName("guide");
		service.updateTag(savedTag);
	}
	
	
	@Test(expected=EntityIsNotExistingException.class)
	public void testDeleteTag_withNotValidId_shouldDeleteTag() throws EntityIsNotExistingException{
		service.deleteTag(23434234l);
	}
	
}
