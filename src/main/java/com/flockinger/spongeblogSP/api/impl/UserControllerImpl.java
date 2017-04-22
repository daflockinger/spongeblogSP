package com.flockinger.spongeblogSP.api.impl;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flockinger.spongeblogSP.api.UserController;
import com.flockinger.spongeblogSP.dto.UserEditDTO;

import io.swagger.annotations.*;

@RestController
public class UserControllerImpl implements UserController{

    public ResponseEntity<List<UserEditDTO>> apiV1UsersGet() {
        // do some magic!
        return new ResponseEntity<List<UserEditDTO>>(HttpStatus.OK);
    }

    public ResponseEntity<UserEditDTO> apiV1UsersPost(@ApiParam(value = "" ,required=true ) @RequestBody UserEditDTO userEdit) {
        // do some magic!
        return new ResponseEntity<UserEditDTO>(HttpStatus.OK);
    }

    public ResponseEntity<Void> apiV1UsersPut(@ApiParam(value = "" ,required=true ) @RequestBody UserEditDTO userEdit) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> apiV1UsersRewindUserIdPut(@ApiParam(value = "Unique identifier of a User;",required=true ) @PathVariable("userId") Long userId) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> apiV1UsersUserIdDelete(@ApiParam(value = "Unique identifier of a User;",required=true ) @PathVariable("userId") Long userId) {
        // do some magic!
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<UserEditDTO> apiV1UsersUserIdGet(@ApiParam(value = "Unique identifier of a User;",required=true ) @PathVariable("userId") Long userId) {
        // do some magic!
        return new ResponseEntity<UserEditDTO>(HttpStatus.OK);
    }

}
