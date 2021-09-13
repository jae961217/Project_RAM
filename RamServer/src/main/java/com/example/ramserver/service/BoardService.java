package com.example.ramserver.service;

import com.example.ramserver.mapper.BoardMapper;
import com.example.ramserver.vo.BoardListVo;
import com.example.ramserver.vo.BoardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    public BoardMapper boardMapper;

    public List<BoardListVo> boardList(int index)
    {
        return boardMapper.boardList(index);
    }

    public BoardVo boardDetail(int boardId)
    {
        return boardMapper.boardDetail(boardId);
    }

    public int register(BoardVo boardVo)
    {
        return boardMapper.register(boardVo);
    }

    public int modify(BoardVo boardVo)
    {
        return boardMapper.modify(boardVo);
    }

    public int check(BoardVo boardVo)
    {
        return boardMapper.check(boardVo);
    }

    public int delete(int boardId)
    {
        return boardMapper.delete(boardId);
    }

    public int max() { return boardMapper.max(); }
}
