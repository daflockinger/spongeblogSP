package com.flockinger.spongeblogSP.service;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.dao.TagDAO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.TagPostsDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.model.Tag;

@Service
public class TagServiceImpl implements TagService{
	
	@Autowired
	private TagDAO dao;
	
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
	public TagPostsDTO getTag(Long id) throws EntityIsNotExistingException{
		if(!dao.exists(id)){
			throw new EntityIsNotExistingException("Tag");
		}
		return mapToPostDto(dao.findOne(id));
	}

	@Override
	@Transactional
	public TagDTO createTag(String name) throws DuplicateEntityException {
		if(isTagExistingAlready(name)){
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
		if(!dao.exists(tag.getId())){
			throw new EntityIsNotExistingException("Tag");
		}
		
		dao.save(map(tag));
	}

	@Override
	@Transactional
	public void deleteTag(Long id) throws EntityIsNotExistingException{
		if(!dao.exists(id)){
			throw new EntityIsNotExistingException("Tag");
		}
		
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
