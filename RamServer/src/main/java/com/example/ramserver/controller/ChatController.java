package com.example.ramserver.controller;

import com.example.ramserver.Response.*;
import com.example.ramserver.model.ChatterInfo;
import com.example.ramserver.model.User;
import com.example.ramserver.service.ChatRoomService;
import com.example.ramserver.service.InsertImgService;
import com.example.ramserver.service.LoginService;
import com.example.ramserver.service.ProfileService;
import com.example.ramserver.vo.ChatRoomVo;
import com.example.ramserver.vo.FindMessageVo;
import com.example.ramserver.vo.ImagePathVo;
import com.example.ramserver.vo.ImageVo;
import org.apache.catalina.webresources.FileResource;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    InsertImgService insertImgService;
    @Autowired
    ProfileService profileService;

    @GetMapping(value="/AllRoom",produces=MediaType.APPLICATION_JSON_VALUE)
    public MultiValueMap<String,String> getRoomInfo(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session=request.getSession();
        User info=(User)session.getAttribute("login");

        //List<String> result=chatRoomService.FindChatter(info.getId());
        MultiValueMap<String,String> form=new LinkedMultiValueMap<>();
        List<ImageVo> result=chatRoomService.MakeResponse(info.getId());
        List<ChatRoomResponse> responseList=new ArrayList<ChatRoomResponse>();
        for(int i=0;i<result.size();i++){
            System.out.println(result.get(i).getImg());
            form.add("id",result.get(i).getId());
            form.add("data",result.get(i).ByteToBase64());
        }
        httpServletResponse.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        //return result;
        return form;
    }

    @GetMapping(value="/AllRoomTest")
    public MultiValueMap<String,Object> getRoomInfoTest(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session=request.getSession();
        User info=(User)session.getAttribute("login");

        MultiValueMap<String,Object> form=new LinkedMultiValueMap<>();
        List<ImageVo> result=chatRoomService.MakeResponse(info.getId());
        List<ChatRoomResponse> responseList=new ArrayList<ChatRoomResponse>();
        for(int i=0;i<result.size();i++){
            form.add("id",result.get(i).getId());
            //form.add("data",result.get(i).ByteToBase64());
            ByteArrayInputStream bis=new ByteArrayInputStream(result.get(i).getImg());
            BufferedImage image= ImageIO.read(bis);
            File outputFile=new File("test.png");
            ImageIO.write(image,"png",outputFile);
            System.out.println(outputFile.getName());
            form.add("data",outputFile);
        }
        httpServletResponse.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        //return result;
        return form;
    }

    @PostMapping(value = "/profileImage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MsgResponse getRoomInfoWithImgage(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;
        MultipartFile file=multipartHttpServletRequest.getFile("file");
        System.out.println(file.getName());
        System.out.println(file.getSize());
        System.out.println(file.getOriginalFilename());
        byte[] data=file.getBytes();
        ImageVo imageVo=new ImageVo("bmh1211@gmail.com",data);
        insertImgService.InsertImage(imageVo);

        MsgResponse response=new MsgResponse();
        response.setMsg("success");
        return response;
    }

    @GetMapping(value="/enterChattingRoom")
    //받은 정보 채팅방 입장한 아이디, 상대방 아이디
    //return 할 정보 : 입장한 사람 아이디, 상대방 아이디,현재까지의 채팅 내용,
    public EnterChatSet EnterChat(HttpServletRequest request,@RequestParam("id") String otherId){
        HttpSession session=request.getSession();
        User info=(User)session.getAttribute("login");
        String enterId=info.getId();
        FindMessageVo findMessageVo=new FindMessageVo(enterId,otherId);
        //List<EnterChatResponse> result=chatRoomService.GetMessage(findMessageVo);
        EnterChatSet result=new EnterChatSet(enterId,chatRoomService.GetMessage(findMessageVo));

        return result;
    }

    @GetMapping(value="/fileMove")
    public void FileMove() throws IOException {
        List<ImageVo> result=chatRoomService.GetAllImageInfo();
        for(int i=0;i<result.size();i++){
            //form.add("data",result.get(i).ByteToBase64());
            ByteArrayInputStream bis=new ByteArrayInputStream(result.get(i).getImg());
            BufferedImage image= ImageIO.read(bis);
            String path="userImage";
            File outputFile=null;
            File dir=new File(path);
            System.out.println(dir.getAbsolutePath());
            if(!dir.exists()){
                System.out.println("존재하지 않습니다.");
                dir.mkdir();
            }
            else{
                System.out.println("존재합니다");
            }
            if(result.get(i).getId().equals("bmh1211@gmail.com")){
                outputFile=new File("userImage/mino.png");
            }
            else if(result.get(i).getId().equals("jae961217@naver.com")){
                outputFile=new File("userImage/jaeyarn.png");
            }
            else if(result.get(i).getId().equals("nemesis747@gmail.com")){
                outputFile=new File("userImage/junyang.png");
            }else if(result.get(i).getId().equals("philippe10@naver.com")){
                outputFile=new File("userImage/hyeonsuk.png");

            }

            FileOutputStream fileOutputStream=new FileOutputStream(outputFile);
            fileOutputStream.write(result.get(i).getImg());
            fileOutputStream.close();
            System.out.println(outputFile.getAbsolutePath());
            /*ImageIO.write(image,"png",outputFile);
            System.out.println(outputFile.getName());*/
        }
    }

    @GetMapping(value="/showimage")
    public MultiValueMap<String,Object> ShowImage(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        HttpSession session=request.getSession();
        User info=(User)session.getAttribute("login");

        MultiValueMap<String,Object> form=new LinkedMultiValueMap<>();
        List<ImagePathVo> result=chatRoomService.GetAllImagePathInfo(info.getId());

        for(int i=0;i<result.size();i++){
            FileInputStream fis=new FileInputStream(result.get(i).getFilePath());
            byte[] res=fis.readAllBytes();
            form.add("id",result.get(i).getId());
            form.add("data", Base64.getEncoder().encodeToString(res));
        }
        httpServletResponse.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        return form;
    }

    //채팅화면에서 사진 클릭함으로써 프로필 접속시 구동되는 로직직
   @GetMapping(value="/profileImage",produces=MediaType.APPLICATION_JSON_VALUE)
    public MultiValueMap<String,Object> EnterProfile(HttpServletResponse request,@RequestParam("id") String userId) throws IOException {
        ProfileResponse result=profileService.GetUserProfile(userId);
        MultiValueMap<String,Object> form=new LinkedMultiValueMap<>();
            FileInputStream fis=new FileInputStream(result.getImagePath());
            byte[] res=fis.readAllBytes();
            form.add("name",result.getName());
            form.add("data", Base64.getEncoder().encodeToString(res));
        request.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        return form;
    }
}
