package com.example.ramserver.mapper;

import com.example.ramserver.Response.ProfileResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProfileMapper {
   public ProfileResponse GetUserProfile(String userId);
}
