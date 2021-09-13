package com.example.ramserver.vo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Base64;

@Data
@AllArgsConstructor
public class ImageVo {
    private String id;
    private byte[] img;

    public String ByteToBase64(){
        byte[] a =img;
        return Base64.getEncoder().encodeToString(img);
    }
}
