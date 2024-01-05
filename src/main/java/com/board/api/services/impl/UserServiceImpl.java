package com.board.api.services.impl;

import com.board.api.entities.Role;
import com.board.api.entities.User;
import com.board.api.exception.AuthException;
import com.board.api.exception.ResourceNotFoundException;
import com.board.api.payloads.UserDto;
import com.board.api.repositories.UserRepo;
import com.board.api.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDto createUser(UserDto user) {
        return null;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        User updateUser = userRepo.save(user);

        return userToDto(updateUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow( ()-> new ResourceNotFoundException("User", " ID ", userId));
        return userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepo.findAll();
        return userList.stream().map(this::userToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userIdToDelete, String email) {
        User userAdmin = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", " Id " + email, 0));
        // System.out.println("the details: " + userAdmin.getAuthorities() + userAdmin.getRole());
        if (userAdmin.getRole() == Role.ADMIN) {
            User user = userRepo.findById(userIdToDelete).orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userIdToDelete));
            userRepo.delete(user);
        } else {
            throw new AuthException("You not have permission to delete a user!");
        }
    }

    public UserDto userToDto(User user){
        //UserDto userDto = new UserDto();
//        userDto.setId(user.getId());
//        userDto.setName(user.getName());
//        userDto.setEmail(user.getEmail());
//        userDto.setPassword(user.getPassword());
//        userDto.setAbout(user.getAbout());
        return modelMapper.map(user, UserDto.class);
    }


    public User dtoToUser(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }
}
