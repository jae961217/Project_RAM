package com.example.ramserver.mapper;

import com.example.ramserver.vo.FindRegionVo;
import com.example.ramserver.vo.PurchaseRegionVo;
import com.example.ramserver.vo.TradeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TradeMapper {
    TradeVo buyList(TradeVo tradeVo);

    TradeVo sellList(TradeVo tradeVo);

    List<PurchaseRegionVo> FindRegion(FindRegionVo findRegionVo);
}
