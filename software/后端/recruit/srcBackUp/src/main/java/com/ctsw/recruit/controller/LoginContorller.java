package com.ctsw.recruit.controller;

import com.ctsw.recruit.mapper.FirmMapper;
import com.ctsw.recruit.pojo.Firm;
import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.pojo.Result;
import com.ctsw.recruit.pojo.User;
import com.ctsw.recruit.service.FirmService;
import com.ctsw.recruit.service.ManagerService;
import com.ctsw.recruit.service.UserService;
import com.ctsw.recruit.utils.JwtUtil;
import com.ctsw.recruit.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//todo  一段时间内保持登陆状态

@RestController
@RequestMapping("")
public class LoginContorller {
    @Autowired
    private UserService userService;
    @Autowired
    private FirmService firmService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    public Result<String> Login(String account, String password, String role) {
        return switch (role) {
            case "User" -> userLogin(account, password);
            case "Firm" -> firmLogin(account, password);
            case "Manager" -> managerLogin(account, password);
            default -> Result.error("登录的身份非法");
        };
    }

//    @PostMapping("/userLogin")
    public Result<String> userLogin(String account, String password){
        //  根据用户名查找user并验证密码的MD5值
        User user = userService.findByAccount(account);
        if(user != null && Md5Util.checkPassword(password, user.getPassword())) {
            //  登录成功
            //todo  重定向
            Map<String, Object> claims = new HashMap<>();
            claims.put("uid", user.getUid());
            claims.put("account", user.getAccount());
            claims.put("role", "User");
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);  //过期时间1个小时

            return Result.success(token);
        }else {
            //  登录失败
            //todo  重定向
            return Result.error("用户名或密码错误");
        }
    }

//    @PostMapping("/firmLogin")
    public Result firmLogin(String account, String password){
        //  根据用户名查找user并验证密码的MD5值
        Firm user = firmService.findByAccount(account);
        if(user != null && Md5Util.checkPassword(password, user.getPassword())) {
            //  登录成功
            //todo  重定向
            Map<String, Object> claims = new HashMap<>();
            claims.put("uid", user.getUid());
            claims.put("account", user.getAccount());
            claims.put("role", "Firm");
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);

            return Result.success(token);
        }else {
            //  登录失败
            //todo  重定向
            return Result.error("用户名或密码错误");
        }
    }

//    @PostMapping("/managerLogin")
    public Result managerLogin(String account, String password){
        //  根据用户名查找user并验证密码的MD5值
        Manager user = managerService.findByAccount(account);
        if(user != null && Md5Util.checkPassword(password, user.getPassword())) {
            //  登录成功
            //todo  重定向
            Map<String, Object> claims = new HashMap<>();
            claims.put("uid", user.getUid());
            claims.put("account", user.getAccount());
            claims.put("role", "Manager");
            String token = JwtUtil.genToken(claims);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);

            return Result.success(token);
        }else {
            //  登录失败
            //todo  重定向
            return Result.error("用户名或密码错误");
        }
    }
}
