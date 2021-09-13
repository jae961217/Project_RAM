package com.example.ramserver.Response;

import com.example.ramserver.vo.BoardListVo;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class BoardListResponse {
    private String msg;
    private List<BoardListVo> list;
}
