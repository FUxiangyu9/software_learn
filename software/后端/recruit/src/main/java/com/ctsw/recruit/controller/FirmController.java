package com.ctsw.recruit.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ctsw.recruit.VO.StatesVO;
import com.ctsw.recruit.enumvalue.JobApplicationStateEnum;
import com.ctsw.recruit.pojo.*;
import com.ctsw.recruit.service.FirmService;
import com.ctsw.recruit.service.PositionService;
import com.ctsw.recruit.service.StatesService;
import com.ctsw.recruit.service.UserService;
import com.ctsw.recruit.utils.Md5Util;
import com.ctsw.recruit.utils.ThreadLocalUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/firm")
@Validated
public class FirmController {
    @Autowired
    private FirmService firmService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private StatesService statesService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/info")
    public Result<Firm> firmInfo(){
        //  从ThreadLocal中获取解析的对象（JWT载荷部分）
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger uid = (BigInteger) map.get("uid");
        QueryWrapper<Firm> queryWrapper = new QueryWrapper<Firm>()
                .eq("firm_uid", uid);
        Firm firm = firmService.getOne(queryWrapper);
        return Result.success(firm);
    }

    @PutMapping("/update")  //@Validated:用于实体类中的注解生效    @RequestBody:按类(跟在注解后的对象)的结构解析请求
    public Result update(@RequestBody @Validated Firm firm){
        //  重设uid，避免越权
        Map<String, Object> map = ThreadLocalUtil.get();
        firm.setFirmUid((BigInteger) map.get("uid"));
        UpdateWrapper<Firm> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("firm_uid", firm.getFirmUid());
        firmService.update(firm, updateWrapper);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger uid = (BigInteger) map.get("uid");
        UpdateWrapper<Firm> updateWrapper = new UpdateWrapper<Firm>()
                .eq("firm_uid", uid)
                .set("firm_avatar_url", avatarUrl);
        firmService.update(updateWrapper);
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
        QueryWrapper<Firm> queryWrapper = new QueryWrapper<Firm>()
                .eq("firm_uid", uid);
        Firm firm = firmService.getOne(queryWrapper);
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
        UpdateWrapper<Firm> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("firm_uid", uid)
                .set("password", newPwd);
        firmService.update(updateWrapper);

        //删除redis中对应的token
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.getOperations().delete(token);

        return Result.success();
    }

    //获取职位list
    @GetMapping("/positionList")
    public Result positionList(){
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger uid = (BigInteger) map.get("uid");
        QueryWrapper<Position> queryWrapper = new QueryWrapper<Position>()
                .eq("firmid", uid);
        List<Position> positions = positionService.list(queryWrapper);
        return Result.success(positions);
    }

    //获取职位的投递数量
    private class DeliveryCnt{
        public Long deliveryNum;
        public Long passNum;
        public Long percentage;
    }
    @GetMapping("/ResumeCount")
    public Result ResumeCount(String positionId){
        QueryWrapper<States> queryWrapper = new QueryWrapper<States>()
                .eq("position_id", positionId);
        Long deliveryNum = statesService.count(queryWrapper);
        queryWrapper.eq("state", JobApplicationStateEnum.ACCEPT);
        Long passNum = statesService.count(queryWrapper);
        DeliveryCnt cnt = new DeliveryCnt();
        cnt.deliveryNum = deliveryNum;
        cnt.passNum = passNum;
        return Result.success(cnt);
    }

    //删除
    @DeleteMapping("/positionDelete")
    public Result positionDelete(String positionId){
        QueryWrapper<Position> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", positionId);
        positionService.remove(queryWrapper);
        return Result.success();
    }

    //添加职位
    @PutMapping("/positionAdd")
    public Result positionAdd(@RequestBody @Validated Position position){
        position.setId(null);
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger firmUid =(BigInteger) map.get("uid");
        position.setFirmid(firmUid);
        positionService.save(position);
        return Result.success();
    }

    //获取职位相关的所有申请
    @GetMapping("/applyListById")
    public Result applyListById(BigInteger positionId){
        if(positionId==null) return Result.error("id为空");
        MPJLambdaWrapper<States> queryWrapper = new MPJLambdaWrapper<States>()
                .eq("position_id", positionId)
                .selectAll(States.class)
                .select(User::getUname)
                .leftJoin(User.class, User::getUid, States::getUserid);
        List<StatesVO> statesWithUnames = statesService.selectJoinList(StatesVO.class, queryWrapper);
        return Result.success(statesWithUnames);
    }

    //通过简历
    @PatchMapping("/passApply")
    public Result passApply(BigInteger positionId, BigInteger userid){
        LocalDateTime dateTime = LocalDateTime.now();
        UpdateWrapper<States> updateWrapper = new UpdateWrapper();
        updateWrapper.allEq(Map.of("position_id", positionId, "userid", userid))
                .set("state", JobApplicationStateEnum.ACCEPT)
                .set("accept_time", dateTime);
        statesService.update(updateWrapper);
        return Result.success();
    }

    //拒绝简历
    @PatchMapping("/rejectApply")
    public Result rejectApply(BigInteger positionId, BigInteger userid){
        LocalDateTime dateTime = LocalDateTime.now();
        UpdateWrapper<States> updateWrapper = new UpdateWrapper();
        updateWrapper.allEq(Map.of("position_id", positionId, "userid", userid))
                .set("state", JobApplicationStateEnum.REJECT)
                .set("accept_time", dateTime);
        statesService.update(updateWrapper);
        return Result.success();
    }

    //拒绝简历
    @PatchMapping("/readTimeUpdate")
    public Result readTimeUpdate(BigInteger positionId, BigInteger userid){
        LocalDateTime dateTime = LocalDateTime.now();
        UpdateWrapper<States> updateWrapper = new UpdateWrapper();
        updateWrapper.allEq(Map.of("position_id", positionId, "userid", userid))
                .set("state", JobApplicationStateEnum.READ)
                .set("read_time", dateTime);
        statesService.update(updateWrapper);
        return Result.success();
    }
}
