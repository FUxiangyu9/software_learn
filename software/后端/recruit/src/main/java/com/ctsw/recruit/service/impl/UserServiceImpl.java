package com.ctsw.recruit.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctsw.recruit.mapper.UserMapper;
import com.ctsw.recruit.pojo.Result;
import com.ctsw.recruit.pojo.Resume;
import com.ctsw.recruit.pojo.User;
import com.ctsw.recruit.service.UserService;
import com.ctsw.recruit.utils.JwtUtil;
import com.ctsw.recruit.utils.Md5Util;
import com.ctsw.recruit.utils.ThreadLocalUtil;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl extends MPJBaseServiceImpl<UserMapper, User> implements UserService {
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
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", user.getUid());
        mapper.update(user, updateWrapper);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger uid = (BigInteger)map.get("uid");
        mapper.updateAvatar(avatarUrl, uid);
    }

    @Override
    public void updatePwd(String pwd, BigInteger uid) {
        mapper.updatePwd(pwd, uid);
    }

    @Override
    public Resume getResume(BigInteger uid) {
        return mapper.getResume(uid);
    }

    @Override
    public void upLoadResume(String uuid, BigInteger uid, String resumeUrl) {
        mapper.upLoadResume(uuid, uid, resumeUrl);
    }

    @Override
    public void updateResume(BigInteger uid, String resumeUrl) {
        mapper.updateResume(uid, resumeUrl);
    }
}
