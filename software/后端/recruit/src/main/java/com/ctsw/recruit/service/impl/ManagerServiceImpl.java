package com.ctsw.recruit.service.impl;

import com.ctsw.recruit.mapper.ManagerMapper;
import com.ctsw.recruit.pojo.Firm;
import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.pojo.Result;
import com.ctsw.recruit.pojo.User;
import com.ctsw.recruit.service.ManagerService;
import com.ctsw.recruit.utils.Md5Util;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl extends MPJBaseServiceImpl<ManagerMapper, Manager> implements ManagerService {
    @Autowired
    private ManagerMapper mapper;

    @Override
    public Manager findByAccount(String account) {
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
    public Result listAllUser(){
        List<User> resList = mapper.getAllUser();
        return Result.success(resList);
    }

    @Override
    public Result listAllFirm(){
        return Result.success(mapper.getAllFirm());
    }

//    @Override
//    public Result listAllFromTable(String tableName) {
//        return super.listAllFromTable(tableName);
//    }
}
