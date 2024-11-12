package com.example.demo.board.domain;

import lombok.Data;

@Data
public class UserBoardResponseDTO {
    // user domain
    private String  id ;

    // board domain
    private Integer bno ;
    private String  title ;
    private String  content ;
    private String  regdate ;
    private Integer viewcnt ;
    private String  writer ;
}
