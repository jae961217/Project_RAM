package com.example.ramserver.controller;

import com.example.ramserver.Response.MsgResponse;
import com.example.ramserver.model.User;
import com.example.ramserver.service.LoginService;
import com.example.ramserver.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/signin")
public class SignInController {

    @Autowired
    LoginService loginService;

    @PostMapping("/submit")
    public MsgResponse postMethod(@RequestBody LoginVo loginVo, HttpServletRequest request){
        HttpSession session=request.getSession();
        //System.out.println(user.getId());
        //User dto=userMapper.findAll();
        //로그인 제출시 body에 담아서 정보 생성
        //System.out.println(searchParam);
        //User userList=
       // return dto.getId();
        User result=loginService.findAll(loginVo);
        MsgResponse response=new MsgResponse();
        if(result==null){
            session.invalidate();//세션 삭제
            //loginResponse.setMsg("Login Failed");
            response.setMsg("failed");
        }
        else {
            session.setAttribute("login",result);
            response.setMsg("success");
        }
        return response;
        //return loginService.findAll(loginVo);
        //return searchParam;
    }
}
