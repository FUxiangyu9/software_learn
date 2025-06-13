package com.ctsw.recruit.service;

import com.ctsw.recruit.pojo.Resume;
import com.ctsw.recruit.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    User findByAccount(String account);
    void register(String account, String password);

    //更新
    void update(User user);

    //更新头像
    void updateAvatar(String avatarUrl);

    //更新密码
    void updatePwd(String pwd, Integer uid);

    //查找自己简历
    Resume getResume(Integer uid);

    //上传简历
    void upLoadResume(String uuid, Integer uid, String resumeUrl);

    //更新简历
    void updateResume(Integer uid, String resumeUrl);
}
