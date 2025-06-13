package com.ctsw.recruit.mapper;

import com.ctsw.recruit.pojo.Firm;
import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ManagerMapper{
    @Select("select * from manager where account=#{account}")
    Manager findByAccount(String account);

    @Insert("insert into manager (account, password)" +
            "values(#{account}, #{password})"
    )
    void add(String account, String password);
    @Select("select * from user")
    List<User> getAllUser();

    @Select("select * from firm")
    List<Firm> getAllFirm();

}
