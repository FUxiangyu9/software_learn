package com.ctsw.recruit.service.impl;

import com.ctsw.recruit.mapper.LogRegMapper;
import com.ctsw.recruit.pojo.Firm;
import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.pojo.User;
import com.ctsw.recruit.service.LogRegService;
import com.ctsw.recruit.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogRegServiceImpl implements LogRegService {
    @Autowired
    private LogRegMapper logRegMapper;
    @Override
    public User findUserByAccount(String account) {
        return logRegMapper.findUserByAccount(account);
    }

    public Firm findFirmByAccount(String account) {
        return logRegMapper.findFirmByAccount(account);
    }

    public Manager findManagerByAccount(String account) {
        return logRegMapper.findManagerByAccount(account);
    }

    @Override
    public void register(String type, String account, String password) {
        //  加密
        String md5String = Md5Util.getMD5String(password);
        //  添加
        logRegMapper.add(type, account, md5String);
    }
}
