package com.flockinger.spongeblogSP.api.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.PostController;
import com.flockinger.spongeblogSP.dto.PostDTO;

import io.swagger.annotations.ApiParam;

@RestController
public class PostControllerImpl implements PostController{

    public ResponseEntity<?> apiV1PostsAuthorUserIdGet(@ApiParam(value = "Unique identifier of a User;",required=true ) @PathVariable("userId") Long userId,
         @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1PostsAuthorUserIdStatusGet(@ApiParam(value = "Unique identifier of a User;",required=true ) @PathVariable("userId") Long userId,
        @ApiParam(value = "Post Status Id",required=true ) @PathVariable("status") String status,
         @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1PostsCategoryCategoryIdGet(@ApiParam(value = "Unique identifier of a Category;",required=true ) @PathVariable("categoryId") Long categoryId,
         @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1PostsCategoryCategoryIdStatusGet(@ApiParam(value = "Unique identifier of a Category;",required=true ) @PathVariable("categoryId") Long categoryId,
        @ApiParam(value = "Post Status Id",required=true ) @PathVariable("status") String status,
         @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1PostsGet( @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1PostsPost(@ApiParam(value = "" ,required=true ) @RequestBody PostDTO postEdit) {
        // do some magic!
        return new ResponseEntity<PostDTO>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1PostsPostIdDelete(@ApiParam(value = "Unique identifier of a Post;",required=true ) @PathVariable("postId") Long postId) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1PostsPostIdGet(@ApiParam(value = "Unique identifier of a Post;",required=true ) @PathVariable("postId") Long postId) {
        // do some magic!
        return new ResponseEntity<PostDTO>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1PostsPut(@ApiParam(value = "" ,required=true ) @RequestBody PostDTO postEdit) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1PostsRewindPostIdPut(@ApiParam(value = "Unique identifier of a Post;",required=true ) @PathVariable("postId") Long postId) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1PostsStatusStatusGet(@ApiParam(value = "Post Status Id",required=true ) @PathVariable("status") String status,
         @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

}
