package com.board.api.controllers;


import com.board.api.payloads.ApiResponse;
import com.board.api.payloads.UserDto;
import com.board.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("back/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/get")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer uid){
        UserDto updateUser = userService.updateUser(userDto, uid);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("del/{userId}/{email}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid, @PathVariable("email") String email){
        userService.deleteUser(uid,email);
        return new ResponseEntity<>(new ApiResponse("user 삭제 성공", true) , HttpStatus.OK);
    }


}
