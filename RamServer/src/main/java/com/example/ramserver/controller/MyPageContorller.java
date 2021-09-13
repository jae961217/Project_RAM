package com.example.ramserver.controller;

import com.example.ramserver.Response.MsgResponse;
import com.example.ramserver.Response.UserResponse;
import com.example.ramserver.model.User;
import com.example.ramserver.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/myPage")
public class MyPageContorller {

    @Autowired
    MyPageService myPageService;

    @GetMapping("/info")
    public UserResponse MyPage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User info = (User)session.getAttribute("login");
        User result = myPageService.myPage(info.getId());
        UserResponse response = new UserResponse();
        if(result == null) {
            response.setMsg("failed");
        }
        else
        {
            response.setMsg("success");
        }
        response.setUser(result);
        return response;
    }

    @PostMapping("/update")
    public MsgResponse Update(@RequestBody User user)
    {
        int result = myPageService.myPageUpdate(user);
        MsgResponse response = new MsgResponse();
        if(result==0)
            response.setMsg("failed");
        else
            response.setMsg("success");

        return response;
    }
}
