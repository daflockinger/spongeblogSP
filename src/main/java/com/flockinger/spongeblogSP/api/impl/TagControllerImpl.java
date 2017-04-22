package com.flockinger.spongeblogSP.api.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.TagController;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.TagPostsDTO;

import io.swagger.annotations.*;

@RestController
public class TagControllerImpl implements TagController {

    public ResponseEntity<?> apiV1TagsGet() {
        // do some magic!
        return new ResponseEntity<List<TagDTO>>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1TagsPost(@ApiParam(value = "" ,required=true ) @RequestBody TagDTO tagEdit) {
        // do some magic!
        return new ResponseEntity<TagDTO>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1TagsPut(@ApiParam(value = "" ,required=true ) @RequestBody TagDTO tagEdit) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1TagsRewindTagIdPut(@ApiParam(value = "Unique identifier of a Tag;",required=true ) @PathVariable("tagId") Long tagId) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1TagsTagIdDelete(@ApiParam(value = "Unique identifier of a Tag;",required=true ) @PathVariable("tagId") Long tagId) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1TagsTagIdGet(@ApiParam(value = "Unique identifier of a Tag;",required=true ) @PathVariable("tagId") Long tagId) {
        // do some magic!
        return new ResponseEntity<TagPostsDTO>(HttpStatus.OK);
    }
}
