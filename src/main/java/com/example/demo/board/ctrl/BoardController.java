package com.example.demo.board.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.board.ctrl.service.BoardService;
import com.example.demo.board.domain.BoardResponseDTO;
import com.example.demo.board.domain.UserBoardResponseDTO;

import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;


@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService ;

    @GetMapping("/list.multicampus")
    public String list(Model model) {
        System.out.println("debug >>> BoardController user endpoint : /board/list.multicampus");
        List<BoardResponseDTO> result = boardService.list();
        System.out.println("debug >>> result size = " + result.size());
        model.addAttribute("boards", result);
        return "listPage" ;
    }

    @GetMapping("/myPage.multicampus")
    public String getMethodName(@RequestParam String id, Model model) {
        System.out.println("debug >>> BoardController user endpoint : /board/myPage.multicampus");
        System.out.println("params : " + id );
        List<UserBoardResponseDTO> result = boardService.history(id);
        System.out.println("debug >>> result size = " + result.size());
        model.addAttribute("boards", result);
        return "listPage" ;
    }
    
}
