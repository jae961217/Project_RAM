package com.example.ramserver.service;


import com.example.ramserver.mapper.JoinMapper;
import com.example.ramserver.vo.JoinVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    @Autowired
    public JoinMapper joinMapper;

    public int join(JoinVo joinVo)
    {
        return joinMapper.join(joinVo);
    }

    public int checkId(String id)
    {
        return joinMapper.checkId(id);
    }

    public int checkNickName(String nickName) { return joinMapper.checkNickName(nickName);}
}
