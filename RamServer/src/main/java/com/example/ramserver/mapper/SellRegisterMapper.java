package com.example.ramserver.mapper;

import com.example.ramserver.vo.SellRegisterVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SellRegisterMapper {
    public void InsertSellingBoard(SellRegisterVo registerVo);
    public int GetBoardCount();
}
