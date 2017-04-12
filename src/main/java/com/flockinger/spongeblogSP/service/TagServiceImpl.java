package com.flockinger.spongeblogSP.service;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dao.TagDAO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.TagPostsDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.model.Tag;
import com.flockinger.spongeblogSP.model.enums.PostStatus;

@Service
public class TagServiceImpl implements TagService {

	@Autowired
	private TagDAO dao;
	
	@Autowired
	private PostDAO postDao;

	@Autowired
	private ModelMapper mapper;

	@Override
	@Transactional(readOnly = true)
	public List<TagDTO> getAllTags() {
		List<TagDTO> tagDTOs = new ArrayList<>();
		Iterable<Tag> tags = dao.findAll();

		tags.iterator().forEachRemaining(tag -> tagDTOs.add(map(tag)));

		return tagDTOs;
	}

	@Override
	@Transactional(readOnly = true)
	public TagPostsDTO getTag(Long id) throws EntityIsNotExistingException {
		if (!dao.exists(id)) {
			throw new EntityIsNotExistingException("Tag");
		}
		Tag tag = dao.findOne(id);
		tag = sortPostsByDateDescending(filterNonPublicPosts(tag));
		return mapToPostDto(tag);
	}

	private Tag sortPostsByDateDescending(Tag sortMe) {

		if (CollectionUtils.isNotEmpty(sortMe.getPosts())) {
			sortMe.getPosts().sort(comparing(Post::getCreated, nullsFirst(naturalOrder())).reversed());
		}
		return sortMe;
	}

	private Tag filterNonPublicPosts(Tag filterMe) {
		if (CollectionUtils.isNotEmpty(filterMe.getPosts())) {
			List<Post> onlyPublicPosts = filterMe.getPosts().stream()
					.filter(post -> Objects.equals(post.getStatus(), PostStatus.PUBLIC))
					.collect(Collectors.toList());
			filterMe.setPosts(onlyPublicPosts);
		}
		return filterMe;
	}

	@Override
	@Transactional
	public TagDTO createTag(String name) throws DuplicateEntityException {
		if (isTagExistingAlready(name)) {
			throw new DuplicateEntityException("Tag");
		}

		Tag tag = new Tag();
		tag.setName(name);

		return map(dao.save(tag));
	}

	private boolean isTagExistingAlready(String name) {
		return isNotEmpty(name) && dao.findByName(name) != null;
	}

	@Override
	@Transactional
	public void updateTag(TagDTO tag) throws EntityIsNotExistingException {
		if (!dao.exists(tag.getId())) {
			throw new EntityIsNotExistingException("Tag");
		}

		dao.save(map(tag));
	}

	@Override
	@Transactional
	public void deleteTag(Long id) throws EntityIsNotExistingException {
		if (!dao.exists(id)) {
			throw new EntityIsNotExistingException("Tag");
		}
		List<Post> postsWithTag = postDao.findByTagsId(id);
		postsWithTag.forEach(post -> {
			post.setTags(
					post.getTags().stream().filter(tag -> tag.getId() != id).collect(Collectors.toList())
					);
			});
		
		postDao.save(postsWithTag);
		
		dao.delete(id);
	}

	private TagPostsDTO mapToPostDto(Tag tag) {
		return mapper.map(tag, TagPostsDTO.class);
	}

	private Tag map(TagDTO tagDto) {
		return mapper.map(tagDto, Tag.class);
	}

	private TagDTO map(Tag tag) {
		return mapper.map(tag, TagDTO.class);
	}
}
