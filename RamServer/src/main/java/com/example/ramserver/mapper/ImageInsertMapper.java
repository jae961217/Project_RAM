package com.example.ramserver.mapper;

import com.example.ramserver.vo.ImageVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageInsertMapper {
    void InsertImage(ImageVo imageVo);
}
