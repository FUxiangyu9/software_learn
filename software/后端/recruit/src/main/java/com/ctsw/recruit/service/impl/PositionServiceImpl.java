package com.ctsw.recruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctsw.recruit.mapper.PositionMapper;
import com.ctsw.recruit.pojo.Position;
import com.ctsw.recruit.service.PositionService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class PositionServiceImpl extends MPJBaseServiceImpl<PositionMapper, Position> implements PositionService {
    @Autowired
    private PositionMapper mapper;
    @Override
    public List<Position> getPositions() {
        return mapper.getPositions();
    }

    @Override
    public Position getPositionById(BigInteger id) {
        return mapper.getPositionById(id);
    }
}
