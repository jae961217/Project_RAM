package com.example.ramserver.service;

import com.example.ramserver.mapper.SellRegisterMapper;
import com.example.ramserver.vo.SellRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellRegisterService {
    @Autowired
    SellRegisterMapper sellRegisterMapper;
    public void InsertSellingBoard(SellRegisterVo registerVo){
        sellRegisterMapper.InsertSellingBoard(registerVo);
    }
    public int GetBoardCount(){
        int a=sellRegisterMapper.GetBoardCount();
        System.out.println(a);
        return a;
    }
}
