package com.example.demo.user.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.user.domain.UserRequestDTO;
import com.example.demo.user.domain.UserResponseDTO;
import com.example.demo.user.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.HashMap;



@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService ;

    @Autowired
    private PasswordEncoder passwordEncoder ;
    
    // index에서 Sign in 버튼을 눌럿을때 경로를 받게함.
    // param값이 post로 id와 pwd 두개 들어옴
    // 값을 인자로 받을때 변수명과 동일해야함
    @PostMapping("/login.multicampus")
    public String login(UserRequestDTO params, 
                        HttpSession session,
                        RedirectAttributes attributes) {

        System.out.println("debug >>> IndexController user endpont : /user/login.multicampus");
        System.out.println("debug >>> params : " + params);
        UserResponseDTO result = userService.login(params);
        System.out.println("debug login result : " + result);

        if(result != null) {
            // 암호화 이후 로그인 처리 구현부
            // 비밀번호 일치 여부를 matches() 메서드를 이용해서 확인
            String userPwd = params.getPwd();
            String encoderPwd = result.getPwd();

            if( passwordEncoder.matches(userPwd, encoderPwd)) {

                System.out.println("debug >>> matches() true");
                // 비밀번호가 같은지 확인하고 비밀번호는 지운채로 넘겨줌
                result.setPwd("");

                // jsp 상태관리(트래킹 매커니즘) - request(포워드되는 페이지까지만 상태정보 유지), session (모든 페이지에서 유지)
                // react 는 session, request 개념이 없음
                session.setAttribute("loginUser", result);
                return "landing" ; 
                // 또 다른 request 또는 페이지로 넘어갈 수 있음(로그인한 사용자만이 볼 수 있는)
            } else {
                // 암호가 일치하지 않는 경우
                // 프론트 쪽에 왜 에러가 났는지 전달시켜줄 필요가 있음
                attributes.addFlashAttribute("loginFail", "비밀번호가 일치하지 않습니다.");
                return "redirect:/" ;
            }
        } else {
            attributes.addFlashAttribute("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "redirect:/";

        }
    }
    

    @GetMapping("/logout.multicampus")
    public String logout(HttpSession session) {
        System.out.println("debug >>> UserController user endpoint : /user/logout.multicampust");
        // 기존 session 삭제(clear)
        session.invalidate();
        return "redirect:/";
    }


    @GetMapping("/join.multicampus")
    public String joinForm() {
        System.out.println("debug >>> UserController user endpont get : /join.multicampus");
        return "join" ;
    }
    

    @PostMapping("/join.multicampus")
    public String postMethodName(@Valid UserRequestDTO params,
                                 BindingResult bindResult,
                                 Model model,
                                 MultipartFile file) {
        System.out.println("debug >>> UserController use endpont post : /join.multicampus");
        if( bindResult.hasErrors() ) {
            System.out.println("debug >>> valid has errers ");
            List<ObjectError> list = bindResult.getAllErrors();
            Map<String, String> map = new HashMap<>();
            for( int idx = 0 ; idx < list.size() ; idx++ ) {
                FieldError field = (FieldError)list.get(idx);
                String key = field.getField();
                String msg = field.getDefaultMessage();
                System.out.println(key + "\t" + msg);
                model.addAttribute(key, msg);
            }
            return "join" ;
        } else {
            // 여기는 유효성 검증을 통과한 값들
            // 비밀번호 암호화 구현
            System.out.println("debug >>> 유효성 통과");
            System.out.println("debug >>> 암호화 객체 = " + passwordEncoder);

            // 암호화 시킨다 = encoding
            System.out.println("debug >>> params = " + params);
            String encoderPwd = passwordEncoder.encode(params.getPwd());
            
            // encoding이 정상적으로 됐는지 확인
            System.out.println("encoderPwd = " +encoderPwd);

            params.setPwd(encoderPwd);
            // file을 추가해서 넘기기
            userService.join(params, file);
            return "redirect:/";
        }
    }
    
    

}
