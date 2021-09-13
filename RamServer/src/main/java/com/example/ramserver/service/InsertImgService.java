package com.example.ramserver.service;

import com.example.ramserver.mapper.ImageInsertMapper;
import com.example.ramserver.vo.ImageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsertImgService {
    @Autowired
    ImageInsertMapper imageInsertMapper;
    public void InsertImage(ImageVo imageVo){
        imageInsertMapper.InsertImage(imageVo);
        return;
    }
}
