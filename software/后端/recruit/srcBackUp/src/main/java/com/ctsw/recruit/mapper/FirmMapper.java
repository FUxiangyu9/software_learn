package com.ctsw.recruit.mapper;

import com.ctsw.recruit.pojo.Firm;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FirmMapper {
    @Select("select * from firm where account=#{account}")
    Firm findByAccount(String account);

    @Insert("insert into firm (account, password)" +
            "values(#{account}, #{password})"
    )
    void add(String account, String password);
}
