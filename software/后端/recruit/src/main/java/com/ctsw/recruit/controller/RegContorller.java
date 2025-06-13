package com.ctsw.recruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctsw.recruit.pojo.Firm;
import com.ctsw.recruit.pojo.Result;
import com.ctsw.recruit.pojo.User;
import com.ctsw.recruit.service.FirmService;
import com.ctsw.recruit.service.UserService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@Validated
public class RegContorller {
    @Autowired
    private UserService userService;
    @Autowired
    private FirmService firmService;

    @PostMapping("/register")
    public Result Register(@Pattern(regexp = "^\\S{5,16}$", message = "用户名长度应在5~16之间") @NotNull String account,
                           @Pattern(regexp = "^\\S{5,16}$", message = "密码长度应在5~16之间") @NotNull String password,
                           @Pattern(regexp = "^\\S{5,16}$", message = "密码长度应在5~16之间") @NotNull String rePassword,
                           String role){
        if(!rePassword.equals(password)){
            return Result.error("两次输入的密码不一致");
        }
        switch (role){
            case "User":{
                return userRegister(account, password);
            }
            case "Firm":{
                return firmRegister(account, password);
            }
            default:{
                return Result.error("注册的身份非法");
            }
        }
    }

//    @PostMapping("/userReg")
    public Result userRegister(@Pattern(regexp = "^\\S{5,16}$", message = "用户名长度应在5~16之间") @NotNull String account,
                               @Pattern(regexp = "^\\S{5,16}$", message = "密码长度应在5~16之间") @NotNull String password){
                                //  @Pattern(regexp = "^\\S{5,16}$") Spring Validation 正则表达式，限制参数长度(account, password)
        //  查询用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>()
                .eq("account", account);
        if(!userService.exists(userQueryWrapper)){
            //没有占用
            //注册
            userService.register(account, password);
            return Result.success();
        }else {
            //占用
            return Result.error("有相同的用户名");
        }
    }
//
//    @PostMapping("/firmReg")
    public Result firmRegister(@Pattern(regexp = "^\\S{5,16}$", message = "用户名长度应在5~16之间")@NotNull String account,
                               @Pattern(regexp = "^\\S{5,16}$", message = "密码长度应在5~16之间") @NotNull String password){
        //  查询用户
        QueryWrapper<Firm> firmQueryWrapper = new QueryWrapper<Firm>()
                .eq("firm_account", account);
        if(!firmService.exists(firmQueryWrapper)){
            //没有占用
            //注册
            firmService.register(account, password);
            return Result.success();
        }else {
            //占用
            return Result.error("有相同的用户名");
        }
    }
//
//    //注释掉管理员注册 路由
////    @PostMapping("/managerReg")
////    public Result managerRegister(String account, String password){
////        //  查询用户
////        Manager user = logRegService.findByAccount("manager", account);
////        if(user == null){
////            //没有占用
////            //注册
////            logRegService.register("manager", account, password);
////            return Result.success();
////        }else {
////            //占用
////            return Result.error("有相同的用户名");
////        }
////    }
}
