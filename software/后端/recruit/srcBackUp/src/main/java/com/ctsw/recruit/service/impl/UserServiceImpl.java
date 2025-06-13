package com.ctsw.recruit.service.impl;

import com.ctsw.recruit.mapper.LogRegMapper;
import com.ctsw.recruit.mapper.UserMapper;
import com.ctsw.recruit.pojo.Result;
import com.ctsw.recruit.pojo.Resume;
import com.ctsw.recruit.pojo.User;
import com.ctsw.recruit.service.UserService;
import com.ctsw.recruit.utils.JwtUtil;
import com.ctsw.recruit.utils.Md5Util;
import com.ctsw.recruit.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper mapper;
    @Override
    public User findByAccount(String account) {
        return mapper.findByAccount(account);
    }

    @Override
    public void register(String account, String password) {
        //  加密
        String md5String = Md5Util.getMD5String(password);
        //  添加
        mapper.add(account, md5String);
    }

    @Override
    public void update(User user) {
        user.setLastTime(LocalDateTime.now());
        mapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer)map.get("uid");
        mapper.updateAvatar(avatarUrl, uid);
    }

    @Override
    public void updatePwd(String pwd, Integer uid) {
        mapper.updatePwd(pwd, uid);
    }

    @Override
    public Resume getResume(Integer uid) {
        return mapper.getResume(uid);
    }

    @Override
    public void upLoadResume(String uuid, Integer uid, String resumeUrl) {
        mapper.upLoadResume(uuid, uid, resumeUrl);
    }

    @Override
    public void updateResume(Integer uid, String resumeUrl) {
        mapper.updateResume(uid, resumeUrl);
    }
}
