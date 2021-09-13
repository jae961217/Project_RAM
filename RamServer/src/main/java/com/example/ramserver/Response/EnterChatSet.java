package com.example.ramserver.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EnterChatSet {
    private String enterId;
    private List<EnterChatResponse> messageSet;


}
