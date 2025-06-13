package com.ctsw.recruit.controller;

import com.ctsw.recruit.pojo.Position;
import com.ctsw.recruit.pojo.Result;
import com.ctsw.recruit.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//    @GetMapping("/getPositions")
//    public String listPosition(Model model){
//        List<Position> pos = positionService.getPositions();
//        model.addAttribute("positions", pos);
//        return "index";
////        return Result.success(pos);
//    }
    @GetMapping("/getPositionById")
    public Result listPosition(BigInteger id){
        Position pos = positionService.getPositionById(id);
        return Result.success(pos);
    }
}
