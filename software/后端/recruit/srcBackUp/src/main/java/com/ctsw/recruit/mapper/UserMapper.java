package com.ctsw.recruit.mapper;

import com.ctsw.recruit.pojo.Resume;
import com.ctsw.recruit.pojo.User;
import org.apache.ibatis.annotations.*;
import org.hibernate.validator.constraints.ru.INN;

import java.util.UUID;

@Mapper
public interface UserMapper {
    @Select("select * from user where account=#{account}")
    User findByAccount(String account);

    @Insert("insert into user (account, password)" +
            "values(#{account}, #{password})"
    )
    void add(String account, String password);

    @Update("update user set uname=#{uname}, " +
            "email=#{email}, " +
            "introduction=#{introduction}, " +
            "tel=#{tel}, " +
            "last_time=#{lastTime} " +
            "where uid=#{uid}"
    )
    void update(User user);

    @Update("update user set avatar_url=#{avatarUrl} where uid=#{uid}")
    void updateAvatar(String avatarUrl, Integer uid);

    @Update("update user set password=#{pwd} where uid = #{uid}")
    void updatePwd(String pwd, Integer uid);

    @Select("select * from resume where uid=#{uid}")
    Resume getResume(Integer uid);

    @Insert("insert into resume (id, uid, resume_url) value (#{uuid}, #{uid}, #{resumeUrl})")
    void upLoadResume(String uuid, Integer uid, String resumeUrl);

    @Update("update resume set resume_url = #{resumeUrl} where uid=#{uid}")
    void updateResume(Integer uid, String resumeUrl);
}
