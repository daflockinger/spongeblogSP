package com.flockinger.spongeblogSP.service;

import java.util.List;

import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;

public interface TagService extends Versionable{
	List<TagDTO> getAllTags();
	TagDTO getTag(Long id) throws EntityIsNotExistingException;
	TagDTO createTag(String name) throws DuplicateEntityException;
	void updateTag(TagDTO tag) throws EntityIsNotExistingException, DuplicateEntityException;
	void deleteTag(Long id) throws EntityIsNotExistingException;
}
