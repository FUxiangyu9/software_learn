package com.ctsw.recruit.service;

import com.ctsw.recruit.pojo.Position;
import com.ctsw.recruit.pojo.States;
import com.github.yulichang.base.MPJBaseService;

import java.math.BigInteger;
import java.util.List;

public interface PositionService extends MPJBaseService<Position> {
    List<Position> getPositions();

    Position getPositionById(BigInteger id);
}
