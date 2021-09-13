package com.example.ramserver.controller;

import com.example.ramserver.Response.BoardListResponse;
import com.example.ramserver.Response.BoardResponse;
import com.example.ramserver.Response.MsgResponse;
import com.example.ramserver.model.User;
import com.example.ramserver.service.BoardService;
import com.example.ramserver.vo.BoardListVo;
import com.example.ramserver.vo.BoardVo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("/list")
    public BoardListResponse list(@RequestParam("index") int index)
    {
        BoardListResponse response = new BoardListResponse();
        List<BoardListVo> result = boardService.boardList(index);
        if(result==null)
            response.setMsg("failed");
        else
            response.setMsg("success");

        response.setList(result);
        return response;
    }

    @GetMapping("/detail")
    public BoardResponse detail(@RequestParam("boardId") int boardId)
    {
        BoardResponse response = new BoardResponse();
        BoardVo result = boardService.boardDetail(boardId);

        if(result==null)
            response.setMsg("failed");
        else
            response.setMsg("success");

        response.setPost(result);
        return response;
    }

    @PostMapping("/register")
    public MsgResponse register(@RequestBody BoardVo boardVo)
    {
        int maxCount = boardService.max();
        boardVo.setBoardId(maxCount+1);
        int result = boardService.register(boardVo);
        MsgResponse response = new MsgResponse();
        if(result == 0)
            response.setMsg("failed");
        else
            response.setMsg("success");

        return response;
    }

    @PostMapping(value = "/Register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MsgResponse Register(HttpServletRequest request) throws IOException, ParseException, java.text.ParseException {
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
        BoardVo boardVo=new BoardVo(boardService.max()+1,info.getId(),jsonObject.get("title").toString(),
                Integer.parseInt(jsonObject.get("price").toString()) ,0,jsonObject.get("body").toString(),
                imgBytes,Regdate);


        int registerResult = boardService.register(boardVo);
        MsgResponse response=new MsgResponse();
        if(registerResult == 0)
            response.setMsg("failed");
        else
            response.setMsg("success");
        return response;
    }


    @PostMapping("/modify")
    public MsgResponse modify(@RequestBody BoardVo boardVo)
    {
        BoardVo boardCheck = new BoardVo(boardVo.getBoardId(), boardVo.getId());

        int check = boardService.check(boardCheck);
        MsgResponse response = new MsgResponse();
        if(check == 0)
            response.setMsg("failed");
        else
        {
            int modifyResult = boardService.modify(boardVo);
            if(modifyResult == 0)
                response.setMsg("failed");
            else
                response.setMsg("success");
        }

        return response;
    }

    @PostMapping("/delete")
    public MsgResponse delete(@RequestBody BoardVo boardVo, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        User info = (User)session.getAttribute("login");
        BoardVo boardCheck = new BoardVo(boardVo.getBoardId(), info.getId());
        int check = boardService.check(boardCheck);
        MsgResponse response = new MsgResponse();
        if(check == 0)
            response.setMsg("failed"); //권한 x
        else
        {
            int deleteResult = boardService.delete(boardVo.getBoardId());
            if(deleteResult == 0)
                response.setMsg("failed");
            else
                response.setMsg("success");
        }
        return response;
    }

    private SimpleDateFormat StringToDate(String date){
        return new SimpleDateFormat(date);
    }
}
