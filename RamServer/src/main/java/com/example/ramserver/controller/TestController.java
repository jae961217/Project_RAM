package com.example.ramserver.controller;

import org.springframework.web.bind.annotation.*;

import java.sql.*;


@RestController
@RequestMapping("/test")
public class TestController {
    private static Connection db;
    PreparedStatement pstmt=null;
    private TestController(){
        db=connectToDB();
    }

    /*@GetMapping("/a")
    public String getParameter(@RequestParam String id, @RequestParam(name="password") String pwd){

    }*/

    private static Connection connectToDB(){
        try{
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try{
            String Url="jdbc:mariadb://3.35.48.170:3306/rotten?autoReconnect=true&verifyServerCertificate=false&useSSL=false";
            String userId="rottenmaster";
            String userPass="hellorottenam1028";
            Connection connection=DriverManager.getConnection(Url,userId,userPass);
            System.out.println("연결 성공");
        } catch (SQLException throwables) {
            System.out.println("연결 실패");
            throwables.printStackTrace();
        }
        return null;
    }

}
