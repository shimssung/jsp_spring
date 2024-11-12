package com.example.demo.user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.user.domain.UserRequestDTO;
import com.example.demo.user.domain.UserResponseDTO;

@Mapper
public interface UserMapper {
    
    public UserResponseDTO loginRow(UserRequestDTO params) ;

    public void joinRow(UserRequestDTO params);
}
