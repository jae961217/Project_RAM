package com.example.ramserver.controller;

import com.example.ramserver.Response.TradeResponse;
import com.example.ramserver.model.User;
import com.example.ramserver.service.TradeService;
import com.example.ramserver.staticvalue.MetroInfo;
import com.example.ramserver.vo.FindRegionVo;
import com.example.ramserver.vo.PurchaseRegionVo;
import com.example.ramserver.vo.TradeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/trade")
public class TradeListController {

    @Autowired
    TradeService tradeService;
    @Autowired
    MetroInfo metroInfo;


    @GetMapping("/list")
    public TradeResponse GetTradeList(HttpServletRequest request, @RequestParam("type") boolean type, @RequestParam("tradeTime") String tradeTime) throws ParseException {
        HttpSession session = request.getSession();
        User info = (User)session.getAttribute("login");

        TradeResponse response = new TradeResponse();
        TradeVo tradeVo = new TradeVo();

        if(type==true)
        {
            tradeVo.setBuyerId(info.getId());
            tradeVo.setTradeTime(StringToDate(tradeTime));
        }
        else
        {
            tradeVo.setSellerId(info.getId());
            tradeVo.setTradeTime(StringToDate(tradeTime));
        }

        TradeVo result = tradeService.list(tradeVo, type);

        if(result==null)
            response.setMsg("failed");
        else
            response.setMsg("success");
        response.setTradeVo(result);

        return response;
    }


    //거래 신청시 지역 위치 푸시
    @GetMapping("/applyPurchase")
    public List<PurchaseRegionVo> SelectRegion(@RequestParam("buyer") String buyer, @RequestParam("owner") String owner){
        List<PurchaseRegionVo> userRegion=tradeService.FindRegion(new FindRegionVo(buyer,owner));

        String res=metroInfo.GetTradePlaceCode("시청","신도림");
        return userRegion;
    }
    private Date StringToDate(String Date) throws ParseException {
        return (new SimpleDateFormat("yyyy-MM-dd")).parse(Date);
    }
}
