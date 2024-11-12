package com.example.demo.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;


@Controller
public class indexController {
    
    /*
    필요에 따라서 메서드가 파라미터값을 전달 받을 때
    @PathVariable, @RequestParam, XXXXDTO를 사용
    JSP는 json으로 받을 일이 없음(그래서 RequsetBody사용 안함)
     */
    @GetMapping("/")
    public String index(HttpSession session) {
        System.out.println("debug >>> IndexController user end point : /");
        if(session.getAttribute("loginUser")!= null) {
            return "landing" ;
        }
        return "index";     // index.jsp를 찾게됨(return으로 index가 넘어가고 application.properties에서 받음)
    }
    
}
