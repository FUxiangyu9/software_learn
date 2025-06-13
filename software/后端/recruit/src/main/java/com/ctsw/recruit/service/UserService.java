package com.ctsw.recruit.service;

//import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ctsw.recruit.pojo.Resume;
import com.ctsw.recruit.pojo.States;
import com.ctsw.recruit.pojo.User;
import com.github.yulichang.base.MPJBaseService;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

public interface UserService extends MPJBaseService<User> {
//public interface UserService {
    User findByAccount(String account);
    void register(String account, String password);

    //更新
    void update(User user);

    //更新头像
    void updateAvatar(String avatarUrl);

    //更新密码
    void updatePwd(String pwd, BigInteger uid);

    //查找自己简历
    Resume getResume(BigInteger uid);

    //上传简历
    void upLoadResume(String uuid, BigInteger uid, String resumeUrl);

    //更新简历
    void updateResume(BigInteger uid, String resumeUrl);
}
