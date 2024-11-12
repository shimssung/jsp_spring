package com.example.demo.board.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.board.domain.BoardResponseDTO;
import com.example.demo.board.domain.UserBoardResponseDTO;

import java.util.List;

@Mapper
public interface BoardMapper {
    public List<BoardResponseDTO> listRow() ;

    public List<UserBoardResponseDTO> myHistoryRow(String id);
}
