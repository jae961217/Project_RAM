package com.example.ramserver.service;


import com.example.ramserver.mapper.TradeMapper;
import com.example.ramserver.vo.FindRegionVo;
import com.example.ramserver.vo.PurchaseRegionVo;
import com.example.ramserver.vo.TradeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    @Autowired
    public TradeMapper tradeMapper;

    public TradeVo list(TradeVo tradeVo, boolean type)
    {
        if(type==true)
            return tradeMapper.buyList(tradeVo);
        else
            return tradeMapper.sellList(tradeVo);
    }

    public List<PurchaseRegionVo> FindRegion(FindRegionVo findRegionVo){
        return tradeMapper.FindRegion(findRegionVo);
    }

}
