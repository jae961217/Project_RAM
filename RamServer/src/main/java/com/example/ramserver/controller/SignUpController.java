package com.example.ramserver.controller;


import com.example.ramserver.Response.MsgResponse;
import com.example.ramserver.service.JoinService;
import com.example.ramserver.vo.JoinVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    JoinService joinService;

    @PostMapping("/submit")
    public MsgResponse postMethod(@RequestBody JoinVo joinVo)
    {
        int result = joinService.join(joinVo);
        MsgResponse response = new MsgResponse();
        if(result == 0)
            response.setMsg("failed");
        else
            response.setMsg("success");



        return response;
    }

    //ID 중복확인
    @GetMapping("/checkId")
    public MsgResponse CheckId(@RequestParam("id") String id)
    {
        int result = joinService.checkId(id);
        MsgResponse response = new MsgResponse();
        if(result == 0)
            response.setMsg("success");
        else
            response.setMsg("failed");
        return response;
    }


    @GetMapping("/checkNickName")
    public MsgResponse CheckNickName(@RequestParam("nickName") String nickName)
    {
        int result = joinService.checkNickName(nickName);
        MsgResponse response = new MsgResponse();
        if(result == 0)
            response.setMsg("success");
        else
            response.setMsg("failed");
        return response;
    }
}
