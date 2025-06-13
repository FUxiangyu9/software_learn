package com.ctsw.recruit.service;

import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.pojo.Result;
import com.ctsw.recruit.pojo.User;

public interface ManagerService {
    Manager findByAccount(String account);
    void register(String account, String password);

    //list all from user
    Result listAllUser();

    //list all from firm
    Result listAllFirm();

//    Result listAllFromTable(String user);
}
