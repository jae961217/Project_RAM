package com.example.ramserver.service;

import com.example.ramserver.mapper.MyPageMapper;
import com.example.ramserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPageService {

    @Autowired
    public MyPageMapper myPageMapper;

    public User myPage(String id)
    {
        return myPageMapper.myPage(id);
    }

    public int myPageUpdate(User user)
    {
        return myPageMapper.myPageUpdate(user);
    }
}
