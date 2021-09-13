package com.example.ramserver.mapper;

import com.example.ramserver.Response.EnterChatResponse;
import com.example.ramserver.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatRoomMapper {
    List<String> FindChatter(String myId);
    List<ImageVo> MakeResponse(String myId);
    List<EnterChatResponse> GetMessage(FindMessageVo findMessageVo);
    int GetMessageCount();
    List<Integer> GetChatRoomId(FindChatRoomVo findChatRoomVo);
    void InsertChatMessage(ChatInsertVo chatInsertVo);
    List<ImageVo> GetAllImageInfo();
    List<ImagePathVo> GetAllImagePathInfo(String myId);
}
