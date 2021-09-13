package com.example.ramserver.service;

import com.example.ramserver.Response.ProfileResponse;
import com.example.ramserver.mapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    ProfileMapper profileMapper;
    public ProfileResponse GetUserProfile(String userId){
        return profileMapper.GetUserProfile(userId);
    }
}
