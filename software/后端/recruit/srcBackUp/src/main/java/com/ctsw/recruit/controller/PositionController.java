package com.ctsw.recruit.controller;

import com.ctsw.recruit.pojo.Position;
import com.ctsw.recruit.pojo.Result;
import com.ctsw.recruit.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/position")
@Validated
public class PositionController {
    @Autowired
    private PositionService positionService;

    @GetMapping("/getPositions")
    public Result listPosition(){
        List<Position> pos = positionService.getPositions();
        return Result.success(pos);
    }
}
