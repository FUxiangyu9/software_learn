package com.ctsw.recruit.controller;


import com.ctsw.recruit.pojo.*;
import com.ctsw.recruit.service.PositionService;
import com.ctsw.recruit.service.UserService;
import com.ctsw.recruit.utils.JwtUtil;
import com.ctsw.recruit.utils.Md5Util;
import com.ctsw.recruit.utils.ThreadLocalUtil;
import io.netty.util.internal.StringUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /*
        获取用户信息
        请求类型：GET
     */
    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        //  从ThreadLocal中获取解析的对象（JWT载荷部分）
        Map<String, Object> map = ThreadLocalUtil.get();
        String account = (String) map.get("account");
        User user = userService.findByAccount(account);
        return Result.success(user);
    }

    /*
        更新用户信息(
        PUT
     */
    @PutMapping("/update")  //@Validated:用于实体类中的注解生效    @RequestBody:按类(跟在注解后的对象)的结构解析请求
    public Result update(@RequestBody @Validated User user){
        //  重设uid，避免越权
        Map<String, Object> map = ThreadLocalUtil.get();
        user.setUid((Integer) map.get("uid"));

        userService.update(user);
        return Result.success();
    }

    /*
        更新用户头像(
        Patch
     */
    //todo 图片化
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    /*
        更新用户密码(
        Patch
        json格式:{
        "oldPwd":
        "newPwd":
        "rePwd":
        }
     */
    @PatchMapping("updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token){
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");
        String rePwd = params.get("rePwd");

        if(oldPwd.isEmpty() || newPwd.isEmpty() || rePwd.isEmpty()){
            return Result.error("缺少必要的参数");
        }

        //原密码是否正确
        //调用userService根据用户名拿到原密码,再和old_pwd比对
        Map<String, Object> map = ThreadLocalUtil.get();
        String account = (String) map.get("account");
        Integer uid = (Integer) map.get("uid");
        User user = userService.findByAccount(account);
        if(!Md5Util.checkPassword(oldPwd, user.getPassword())){
            return Result.error("旧密码错误");
        }

        //验证新密码是否一致
        if(!newPwd.equals(rePwd)){
            return Result.error("两次输入的新密码不一致");
        }

        //成功
        //进行更新
        newPwd = Md5Util.getMD5String(newPwd);
        userService.updatePwd(newPwd, uid);

        //删除redis中对应的token
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.getOperations().delete(token);

        return Result.success();
    }

    /*
        查看个人简历(
        get

     */
    @GetMapping("/resume")
    public Result getResume(){
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("uid");
        Resume resume = userService.getResume(uid);
        if(resume == null){
            return Result.error("没有简历");
        }
        return Result.success(resume);
    }

    /*
        插入个人简历(每人拥有至多一份
        get

     */
    @PatchMapping("/upLoadResume")
    public Result upLoadResume(@RequestParam @URL String resumeUrl){
        Resume resume = (Resume) getResume().getData();
        if(resume != null){
            return Result.error("已经存在一份简历");
        }

        String uuid = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> map = ThreadLocalUtil.get();
        String account = (String) map.get("account");
        Integer uid = (Integer) map.get("uid");
        userService.upLoadResume(uuid, uid, resumeUrl);
        return Result.success();
    }

    /*
        更新个人简历(每人拥有至多一份
        get

     */
    @PatchMapping("/updateResume")
    public Result updateResume(@RequestParam @URL String resumeUrl){
        Resume resume = (Resume) getResume().getData();
        if(resume == null){
            return Result.error("没有简历");
        }
        Map<String, Object> map = ThreadLocalUtil.get();
        String account = (String) map.get("account");
        Integer uid = (Integer) map.get("uid");
        userService.updateResume(uid, resumeUrl);
        return Result.success();
    }

}
