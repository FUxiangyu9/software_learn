package com.ctsw.recruit.mapper;

import com.ctsw.recruit.pojo.Firm;
import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.pojo.Result;
import com.ctsw.recruit.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface BaseMapper<T>{
    //  根据用户名查找用户
    @Select("select * from user where account=#{account}")
    User findUserByAccount(String account);

    @Select("select * from firm where account=#{account}")
    Firm findFirmByAccount(String account);

    @Select("select * from manager where account=#{account}")
    Manager findManagerByAccount(String account);
}
