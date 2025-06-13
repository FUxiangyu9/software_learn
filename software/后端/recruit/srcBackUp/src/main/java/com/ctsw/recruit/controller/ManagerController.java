package com.ctsw.recruit.controller;

import com.ctsw.recruit.pojo.Result;
import com.ctsw.recruit.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @GetMapping("/list/{tableName}")
    //todo 条件查询
    public Result list(@PathVariable String tableName){
        switch (tableName){
            case "user":    //必须验证后才能查询，避免sql注入
                return managerService.listAllUser();

            case "firm":
                return managerService.listAllFirm();

            default:
                return Result.error("非法访问");
        }
    }
}
