package com.ctsw.recruit.service.impl;

import com.ctsw.recruit.mapper.FirmMapper;
import com.ctsw.recruit.pojo.Firm;
import com.ctsw.recruit.pojo.User;
import com.ctsw.recruit.service.FirmService;
import com.ctsw.recruit.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirmServiceImpl implements FirmService {
    @Autowired
    private FirmMapper mapper;
    @Override
    public Firm findByAccount(String account) {
        return mapper.findByAccount(account);
    }

    @Override
    public void register(String account, String password) {
        //  加密
        String md5String = Md5Util.getMD5String(password);
        //  添加
        mapper.add(account, md5String);
    }
}
