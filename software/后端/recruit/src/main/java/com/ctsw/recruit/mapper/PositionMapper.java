package com.ctsw.recruit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctsw.recruit.pojo.Position;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.List;

//@Mapper
public interface PositionMapper extends MPJBaseMapper<Position> {
    @Select("select position.id, position.firmid, position.job_name, " +
            "position.payroll, position.city, position.degree, position.hc , firm_corp " +
            "from position left join firm on position.firmid=firm.firm_uid ")
    List<Position> getPositions();

    @Select("select position.*, firm.firm_corp " +
            "from position  left join firm on position.firmid=firm.firm_uid " +
            "where position.id = #{id}")
    Position getPositionById(BigInteger id);
}
