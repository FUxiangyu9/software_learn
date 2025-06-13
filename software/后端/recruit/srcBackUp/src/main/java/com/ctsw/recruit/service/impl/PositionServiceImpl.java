package com.ctsw.recruit.service.impl;

import com.ctsw.recruit.mapper.PositionMapper;
import com.ctsw.recruit.pojo.Position;
import com.ctsw.recruit.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionMapper mapper;
    @Override
    public List<Position> getPositions() {
        return mapper.getPositions();
    }
}
