package com.example.ramserver.service;

import com.example.ramserver.mapper.LoginMapper;
import com.example.ramserver.model.User;
import com.example.ramserver.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    public LoginMapper loginMapper;

    public User findAll(LoginVo loginVo)
    {
        return loginMapper.findAll(loginVo);
       /* if(foundUser==null){
            return "Login failed";
        }
        else
        {
            return "Login Success";
        }*/
        //return loginMapper.findAll(loginVo);
    }
}
