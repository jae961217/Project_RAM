package com.example.ramserver.controller;

import com.example.ramserver.model.SearchParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GetController {
    @RequestMapping(method= RequestMethod.GET,path="/getMethod")//localhost:8080/api/getMethod
    public String getRequest(){

        return "Hi GetMethod";
    }
    @GetMapping("/getParameter")
    public String getParameter(@RequestParam String id, @RequestParam(name="password") String pwd){
        System.out.println("id : "+id);
        System.out.println("password: "+pwd);

        return id+pwd;
    }

    @GetMapping("/getMultiParameter")
    public SearchParam getMultiParamter(SearchParam searchParam){
        System.out.println(searchParam.getAccount());
        System.out.println(searchParam.getEmail());
        System.out.println(searchParam.getPage());

        // {"Account" : "", "email": " ","page": 0}
        return searchParam; //json 형태 데이터 전달
        //return "model Data";
    }
}
