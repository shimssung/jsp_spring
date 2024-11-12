package com.example.demo.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.user.dao.UserMapper;
import com.example.demo.user.domain.UserRequestDTO;
import com.example.demo.user.domain.UserResponseDTO;

import java.util.UUID ;

import java.io.File ;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper ;
    
    public UserResponseDTO login(UserRequestDTO params) {
        System.out.println("debug >>> UserService login");
        return userMapper.loginRow(params) ;
    }

    // 고민?
    // 1. elfprxhfl, 2. 파일이름
    public void join(UserRequestDTO params, MultipartFile file) {
        System.out.println("debug >>> UserService join");
        System.out.println("debug >>> upload img : " + file.getOriginalFilename());

        String path = "C:\\00.fullstack\\2.java\\eclipse-workspace\\jsp_spring\\src\\main\\resources\\static\\resources\\img\\" ;

        UUID uuid = UUID.randomUUID() ;
        String fileName = uuid+"_"+file.getOriginalFilename() ;
        System.out.println("debug >>> upload uuid img : " + fileName);

        // 이미지를 지정 디렉토리 밑에 파일이름으로 저장
        // 파일 객체 생성
        File saveFile = new File(path, fileName);
        try {
            file.transferTo(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.setImgUrl(fileName);
        userMapper.joinRow(params);
    }
}
