package com.example.ramserver.Response;

import com.example.ramserver.vo.BoardVo;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class BoardResponse {
    private String msg;
    private BoardVo post;
}
