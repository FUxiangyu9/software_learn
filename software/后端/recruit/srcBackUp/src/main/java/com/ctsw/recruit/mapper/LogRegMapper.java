package com.ctsw.recruit.mapper;

import com.ctsw.recruit.pojo.Firm;
import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LogRegMapper {
    //  添加用户
    @Insert("insert into ${tableName}(account, password)" +
            "values(#{account}, #{password})"
    )
    void add(@Param("tableName") String tableName, String account, String password);

    //  根据用户名查找用户
    @Select("select * from user where account=#{account}")
    User findUserByAccount(String account);

    @Select("select * from firm where account=#{account}")
    Firm findFirmByAccount(String account);

    @Select("select * from manager where account=#{account}")
    Manager findManagerByAccount(String account);

}
