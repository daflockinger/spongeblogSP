package com.flockinger.spongeblogSP.api.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.CategoryController;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.CategoryPostsDTO;

import io.swagger.annotations.ApiParam;

@RestController
public class CategoryControllerImpl implements CategoryController{

    public ResponseEntity<?> apiV1CategoriesCategoryIdDelete(@ApiParam(value = "Unique identifier of a Category;",required=true ) @PathVariable("categoryId") Long categoryId) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1CategoriesCategoryIdGet(@ApiParam(value = "Unique identifier of a Category;",required=true ) @PathVariable("categoryId") Long categoryId) {
        // do some magic!
        return new ResponseEntity<CategoryPostsDTO>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1CategoriesChildrenParentCategoryIdGet(@ApiParam(value = "Unique identifier of the parent Category;",required=true ) @PathVariable("parentCategoryId") Long parentCategoryId) {
        // do some magic!
        return new ResponseEntity<List<CategoryPostsDTO>>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1CategoriesGet() {
        // do some magic!
        return new ResponseEntity<List<CategoryDTO>>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1CategoriesPost(@ApiParam(value = "" ,required=true ) @RequestBody CategoryDTO categoryEdit) {
        // do some magic!
        return new ResponseEntity<CategoryDTO>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1CategoriesPut(@ApiParam(value = "" ,required=true ) @RequestBody CategoryDTO categoryEdit) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> apiV1CategoriesRewindCategoryIdPut(@ApiParam(value = "Unique identifier of a Category;",required=true ) @PathVariable("categoryId") Long categoryId) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
