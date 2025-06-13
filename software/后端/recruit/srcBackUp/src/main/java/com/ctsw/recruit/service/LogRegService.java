package com.ctsw.recruit.service;

import com.ctsw.recruit.pojo.Firm;
import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.pojo.User;

//  interface类,使用public interface
public interface LogRegService {
    // 根据用户名查找用户
    //  type为对应用户表的名
    User findUserByAccount(String account);
    Firm findFirmByAccount(String account);
    Manager findManagerByAccount(String account);

    void register(String type, String account, String password);
}
