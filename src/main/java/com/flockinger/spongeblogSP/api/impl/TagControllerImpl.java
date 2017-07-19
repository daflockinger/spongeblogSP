package com.flockinger.spongeblogSP.api.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.TagController;
import com.flockinger.spongeblogSP.api.util.RequestValidator;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.service.TagService;

import io.swagger.annotations.ApiParam;

@RestController
public class TagControllerImpl implements TagController {

  @Autowired
  private TagService service;

  @Autowired
  private RequestValidator validator;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public ResponseEntity<?> apiV1TagsGet() {

    List<TagDTO> tags = service.getAllTags();
    tags.forEach(tag -> addSelfLink(tag));
    return new ResponseEntity<List<TagDTO>>(tags, HttpStatus.OK);
  }

  public ResponseEntity<?> apiV1TagsPost(
      @ApiParam(value = "", required = true) @Valid @RequestBody TagDTO tagEdit,
      BindingResult bindingResult) throws DuplicateEntityException, DtoValidationFailedException {

    validator.validateRequestBody(bindingResult);
    TagDTO tag = service.createTag(tagEdit.getName());
    return new ResponseEntity<TagDTO>(addSelfLink(tag), HttpStatus.CREATED);
  }

  public ResponseEntity<?> apiV1TagsPut(
      @ApiParam(value = "", required = true) @Valid @RequestBody TagDTO tagEdit,
      BindingResult bindingResult)
      throws EntityIsNotExistingException, DtoValidationFailedException, DuplicateEntityException {

    validator.validateRequestBody(bindingResult);
    validator.assertIdForUpdate(tagEdit.getTagId());
    service.updateTag(tagEdit);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  public ResponseEntity<?> apiV1TagsRewindTagIdPut(@ApiParam(value = "Unique identifier of a Tag;",
      required = true) @PathVariable("tagId") Long tagId) throws NoVersionFoundException {

    service.rewind(tagId);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  public ResponseEntity<?> apiV1TagsTagIdDelete(@ApiParam(value = "Unique identifier of a Tag;",
      required = true) @PathVariable("tagId") Long tagId) throws EntityIsNotExistingException {
    service.deleteTag(tagId);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  public ResponseEntity<?> apiV1TagsTagIdGet(@ApiParam(value = "Unique identifier of a Tag;",
      required = true) @PathVariable("tagId") Long tagId) throws EntityIsNotExistingException {

    TagDTO tag = service.getTag(tagId);
    return new ResponseEntity<TagDTO>(addSelfLink(tag), HttpStatus.OK);
  }

  private TagDTO addSelfLink(TagDTO tag) {
    try {
      tag.add(linkTo(methodOn(TagControllerImpl.class).apiV1TagsTagIdGet(tag.getTagId()))
          .withSelfRel());
    } catch (EntityIsNotExistingException e) {
      logger.error("Not found after Persisting. Should not happen.");
    }
    return tag;
  }
}
