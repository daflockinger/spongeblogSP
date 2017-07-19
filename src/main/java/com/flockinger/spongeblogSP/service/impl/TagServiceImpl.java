package com.flockinger.spongeblogSP.service.impl;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dao.TagDAO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.model.Tag;
import com.flockinger.spongeblogSP.service.TagService;
import com.flockinger.spongeblogSP.service.VersioningService;

@Service
public class TagServiceImpl implements TagService {

  @Autowired
  private TagDAO dao;

  @Autowired
  private PostDAO postDao;

  @Autowired
  private ModelMapper mapper;

  @Autowired
  private VersioningService<Tag, TagDAO> versionService;

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
  public TagDTO getTag(Long id) throws EntityIsNotExistingException {
    if (!dao.exists(id)) {
      throw new EntityIsNotExistingException("Tag");
    }
    Tag tag = dao.findOne(id);
    return map(tag);
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
  public void updateTag(TagDTO tag) throws EntityIsNotExistingException, DuplicateEntityException {
    if (!dao.exists(tag.getTagId())) {
      throw new EntityIsNotExistingException("Tag");
    }
    if (isTagExistingWithOtherId(tag)) {
      throw new DuplicateEntityException("Tag");
    }

    dao.save(map(tag));
  }

  private boolean isTagExistingWithOtherId(TagDTO toUpdateTag) {
    Tag tag = dao.findByName(toUpdateTag.getName());
    return tag != null && tag.getId() != toUpdateTag.getTagId();
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
          post.getTags().stream().filter(tag -> tag.getId() != id).collect(Collectors.toList()));
    });

    postDao.save(postsWithTag);

    dao.delete(id);
  }

  @Override
  public void rewind(Long id) throws NoVersionFoundException {
    versionService.rewind(id, dao);
  }

  private Tag map(TagDTO tagDto) {
    return mapper.map(tagDto, Tag.class);
  }

  private TagDTO map(Tag tag) {
    return mapper.map(tag, TagDTO.class);
  }
}
