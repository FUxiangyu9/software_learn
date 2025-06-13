package com.ctsw.recruit.mapper;

//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctsw.recruit.pojo.Resume;
import com.ctsw.recruit.pojo.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.*;
import org.hibernate.validator.constraints.ru.INN;

import java.math.BigInteger;
import java.util.UUID;

//@Mapper
public interface UserMapper extends MPJBaseMapper<User> {
//public interface UserMapper{
    @Select("select * from user where account=#{account}")
    User findByAccount(String account);

    @Insert("insert into user (account, password)" +
            "values(#{account}, #{password})"
    )
    void add(String account, String password);

//    @Update("update user set uname=#{uname}, " +
//            "email=#{email}, " +
//            "introduction=#{introduction}, " +
//            "tel=#{tel}, " +
//            "last_time=#{lastTime}, " +
//            "sex=#{sex} " +
//            "where uid=#{uid}"
//    )
//    void update(User user);

    @Update("update user set avatar_url=#{avatarUrl} where uid=#{uid}")
    void updateAvatar(String avatarUrl, BigInteger uid);

    @Update("update user set password=#{pwd} where uid = #{uid}")
    void updatePwd(String pwd, BigInteger uid);

    @Select("select * from resume where uid=#{uid}")
    Resume getResume(BigInteger uid);

    @Insert("insert into resume (id, uid, resume_url) value (#{uuid}, #{uid}, #{resumeUrl})")
    void upLoadResume(String uuid, BigInteger uid, String resumeUrl);

    @Update("update resume set resume_url = #{resumeUrl} where uid=#{uid}")
    void updateResume(BigInteger uid, String resumeUrl);
}
