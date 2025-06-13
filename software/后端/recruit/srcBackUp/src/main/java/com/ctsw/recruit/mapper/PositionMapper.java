package com.ctsw.recruit.mapper;

import com.ctsw.recruit.pojo.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PositionMapper {
    @Select("select * from position")
    List<Position> getPositions();
}
