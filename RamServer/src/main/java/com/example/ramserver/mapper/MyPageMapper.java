package com.example.ramserver.mapper;

import com.example.ramserver.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyPageMapper {
    User myPage(String id);

    int myPageUpdate(User user);

}
