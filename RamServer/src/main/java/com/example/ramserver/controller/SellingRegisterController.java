package com.example.ramserver.controller;

import com.example.ramserver.Response.MsgResponse;
import com.example.ramserver.model.User;
import com.example.ramserver.service.SellRegisterService;
import com.example.ramserver.vo.SellRegisterVo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/SellingRegister")
public class SellingRegisterController {
    @Autowired
    SellRegisterService sellRegisterService;

    @PostMapping(value = "/Register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MsgResponse RegisterSelling(HttpServletRequest request) throws IOException, ParseException, java.text.ParseException {
        HttpSession session=request.getSession();
        User info=(User)session.getAttribute("login");
        MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;
        List<MultipartFile> files=multipartHttpServletRequest.getFiles("file");


        JSONObject jsonObject=null;
        byte[] imgBytes=null;
       for(int i=0;i<files.size();i++){
           byte[] data =files.get(i).getBytes();
           if(files.get(i).getOriginalFilename().equals("json")){
               JSONParser parser=new JSONParser();
               Object obj=parser.parse(new String(data));
               jsonObject=(JSONObject)obj;
           }else{
                imgBytes=data;
           }
       }
       String Regdate01=StringToDate(jsonObject.get("date").toString()).format(new Date());
       Date Regdate=StringToDate(jsonObject.get("date").toString()).parse(Regdate01);
        SellRegisterVo sellRegisterVo=new SellRegisterVo(sellRegisterService.GetBoardCount()+1,info.getId(),jsonObject.get("title").toString(),
                Integer.parseInt(jsonObject.get("price").toString()) ,0,jsonObject.get("body").toString(),
                imgBytes,Regdate);
        sellRegisterService.InsertSellingBoard(sellRegisterVo);
        MsgResponse response=new MsgResponse();
        response.setMsg("Register success");
        return response;
    }

    private SimpleDateFormat StringToDate(String date){
        return new SimpleDateFormat(date);
    }
}
