package com.example.ramserver.mapper;

import com.example.ramserver.vo.BoardListVo;
import com.example.ramserver.vo.BoardVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardListVo> boardList(int index);
    BoardVo boardDetail(int boardId);
    int register(BoardVo boardVo);
    int modify(BoardVo boardVo);
    int delete(int boardId);
    int check(BoardVo boardVo);
    int max();
}
