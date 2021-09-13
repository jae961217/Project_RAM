package com.example.ramserver.controller;

import com.example.ramserver.model.SearchParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {

    //post 는  HTml <Form> 태그시
    //ajax 검색시
    //http post body에 데이터 넣어서 전달
    //json, xml, multipoart-form /text-plain
    @PostMapping(value = "/postMethod")
    public SearchParam postMethod(@RequestBody SearchParam searchParam){
        return searchParam;
    }


    //put pathc, ->body값 업데이트
    @PutMapping("/putMethod")
    public void put(){

    }

    @PatchMapping("/patchMethod")
    public void patch(){

    }

    //rest의 개념
    //http 프로토콜에 있는 method를 활용한 아키텍처
    //http 메소드를 통해 리소스처리
    //crud를 통한 리소스 조작시
    // get => 조회(Select * READ) /user/{id}
    // post => 생성(Create) /user
    // put/pathc => 수정(Update *Create) /user
    // delete -> 삭제 (Delete) /user/{1}
}
