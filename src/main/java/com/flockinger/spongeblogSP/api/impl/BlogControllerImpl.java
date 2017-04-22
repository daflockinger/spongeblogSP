package com.flockinger.spongeblogSP.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.BlogController;
import com.flockinger.spongeblogSP.dto.BlogDTO;

import io.swagger.annotations.ApiParam;

@RestController
public class BlogControllerImpl implements BlogController{

    public ResponseEntity<?> apiV1BlogDelete() {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1BlogGet() {
        // do some magic!
        return new ResponseEntity<BlogDTO>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1BlogPost(@ApiParam(value = "" ,required=true ) @RequestBody BlogDTO blogEdit) {
        // do some magic!
        return new ResponseEntity<BlogDTO>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1BlogPut(@ApiParam(value = "" ,required=true ) @RequestBody BlogDTO blogEdit) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1BlogRewindPut() {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
