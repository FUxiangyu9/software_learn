package com.ctsw.recruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ctsw.recruit.mapper.ManagerMapper;
import com.ctsw.recruit.pojo.*;
import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.service.FirmService;
import com.ctsw.recruit.service.ManagerService;
import com.ctsw.recruit.service.UserService;
import com.ctsw.recruit.utils.Md5Util;
import com.ctsw.recruit.utils.ThreadLocalUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private UserService userService;
    @Autowired
    private FirmService firmService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //提供调用获取用户信息接口的函数
    @GetMapping("/info")
    public Result Info(){
        //  从ThreadLocal中获取解析的对象（JWT载荷部分）
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger uid = (BigInteger) map.get("uid");
        QueryWrapper<Manager> queryWrapper = new QueryWrapper<Manager>()
                .eq("uid", uid);
        Manager manager = managerService.getOne(queryWrapper);
        return Result.success(manager);
    }

    @PutMapping("/update")  //@Validated:用于实体类中的注解生效    @RequestBody:按类(跟在注解后的对象)的结构解析请求
    public Result update(@RequestBody @Validated Manager manager){
        //  重设uid，避免越权
        Map<String, Object> map = ThreadLocalUtil.get();
        manager.setUid((BigInteger) map.get("uid"));
        UpdateWrapper<Manager> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", manager.getUid());
        managerService.update(manager, updateWrapper);
//        managerService.update(manager);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger uid = (BigInteger) map.get("uid");
        UpdateWrapper<Manager> updateWrapper = new UpdateWrapper<Manager>()
                .eq("uid", uid)
                .set("avatar_url", avatarUrl);
        managerService.update(updateWrapper);
        return Result.success();
    }

    @PatchMapping("updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token){
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");
        String rePwd = params.get("rePwd");

        if(oldPwd.isEmpty() || newPwd.isEmpty() || rePwd.isEmpty()){
            return Result.error("缺少必要的参数");
        }

        //原密码是否正确
        //调用firmService根据用户名拿到原密码,再和old_pwd比对
        Map<String, Object> map = ThreadLocalUtil.get();
        String account = (String) map.get("account");
        BigInteger uid = (BigInteger) map.get("uid");
        QueryWrapper<Manager> queryWrapper = new QueryWrapper<Manager>()
                .eq("uid", uid);
        Manager firm = managerService.getOne(queryWrapper);
        if(!Md5Util.checkPassword(oldPwd, firm.getPassword())){
            return Result.error("旧密码错误");
        }

        //验证新密码是否一致
        if(!newPwd.equals(rePwd)){
            return Result.error("两次输入的新密码不一致");
        }

        //成功
        //进行更新
        newPwd = Md5Util.getMD5String(newPwd);
        UpdateWrapper<Manager> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", uid)
                .set("password", newPwd);
        managerService.update(updateWrapper);

        //删除redis中对应的token
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.getOperations().delete(token);

        return Result.success();
    }

    //得到用户列表
    @GetMapping("/userList")
    public Result userList(){
        List<User> list = userService.list();
        return Result.success(list);
    }

    //删除用户
    @DeleteMapping("/userDelete")
    public Result userDelete(BigInteger uid){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("uid", uid);
        userService.remove(userQueryWrapper);
        return Result.success();
    }

    //得到企业列表
    @GetMapping("/firmList")
    public Result firmList(){
        List<Firm> list = firmService.list();
        return Result.success(list);
    }

    //删除用户
    @DeleteMapping("/firmDelete")
    public Result firmDelete(BigInteger uid){
        QueryWrapper<Firm> firmQueryWrapper = new QueryWrapper<>();
        firmQueryWrapper.eq("uid", uid);
        firmService.remove(firmQueryWrapper);
        return Result.success();
    }

    //搜索用户
    @GetMapping("/userListBySelect")
    public Result userListBySelect(int selectType, String input){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        switch (selectType){
            case 1: queryWrapper.like("uid", input); break;
            case 2: queryWrapper.like("account", input); break;
            case 3: queryWrapper.like("uname", input); break;
            case 4: queryWrapper.like("tel", input);
        }
        List<User> userList = userService.list(queryWrapper);
        return Result.success(userList);
    }

    //搜索企业
    @GetMapping("/firmListBySelect")
    public Result firmListBySelect(int selectType, String input){
        QueryWrapper<Firm> queryWrapper = new QueryWrapper<>();
        switch (selectType){
            case 1: queryWrapper.like("firm_uid", input); break;
            case 2: queryWrapper.like("firm_account", input); break;
            case 3: queryWrapper.like("firm_uname", input); break;
            case 4: queryWrapper.like("firm_tel", input);break;
            case 5: queryWrapper.like("firm_corp", input);break;
        }
        List<Firm> firmList = firmService.list(queryWrapper);
        return Result.success(firmList);
    }
}
