package com.example.ramserver.mapper;

import com.example.ramserver.model.User;
import com.example.ramserver.vo.LoginVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginMapper {
    User findAll(LoginVo loginVo);

}
