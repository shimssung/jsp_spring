package com.example.demo.api.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/naver/api")
public class ApiMainController {
    
    @GetMapping("/search")
    public String getMethodName() {
        System.out.println("debug >>> user endpoint : /naver/api/search");
        return "search_api";
    }
    
}
