package com.example.ramserver.Response;

import com.example.ramserver.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private String msg;

    private User user;
}
