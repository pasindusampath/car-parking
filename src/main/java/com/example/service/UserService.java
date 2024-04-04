package com.example.service;

import com.example.dao.UserDAO;
import com.example.dto.UserDTO;
import com.example.entity.custom.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    UserDAO dao;
    public UserService(){
        dao = new UserDAO();
    }

    public boolean addUser(UserDTO userDTO){
        ModelMapper modelMapper = new ModelMapper();
        return dao.createUser(modelMapper.map(userDTO, User.class));

    }

    public boolean updateUser(UserDTO userDTO){
        ModelMapper modelMapper = new ModelMapper();
        return dao.updateUser(modelMapper.map(userDTO, User.class));
    }

    public UserDTO searchUser(String uid){
        User userById = dao.getUserById(uid);
        if (userById==null)return null;
        return new ModelMapper().map(userById,UserDTO.class);
    }

    public List<UserDTO> getAll(){
        List<User> allUsers = dao.getAllUsers();
        return new ModelMapper().map(allUsers,new TypeToken<ArrayList<UserDTO>>(){}.getType());
    }

    public boolean deleteUser(String id){
      return  dao.deleteUser(id);
    }

    public String generateNewUserId(){
        return dao.generateNewUserId();
    }



}
