package com.ctsw.recruit.mapper;

import com.ctsw.recruit.pojo.Firm;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

//@Mapper
public interface FirmMapper extends MPJBaseMapper<Firm> {
    @Select("select * from firm where firm_account=#{account}")
    Firm findByAccount(String account);

    @Insert("insert into firm (firm_account, password)" +
            "values(#{account}, #{password})"
    )
    void add(String account, String password);
}
