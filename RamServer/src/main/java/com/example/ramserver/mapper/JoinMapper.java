package com.example.ramserver.mapper;


import com.example.ramserver.vo.JoinVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JoinMapper {

    int checkId(String id);

    int join(JoinVo joinVo);

    int checkNickName(String nickName);
}
