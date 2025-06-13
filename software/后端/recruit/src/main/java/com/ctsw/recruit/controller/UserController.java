package com.ctsw.recruit.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ctsw.recruit.VO.StatesVO;
import com.ctsw.recruit.enumvalue.JobApplicationStateEnum;
import com.ctsw.recruit.pojo.*;
import com.ctsw.recruit.service.PositionService;
import com.ctsw.recruit.service.ResumeService;
import com.ctsw.recruit.service.StatesService;
import com.ctsw.recruit.service.UserService;
import com.ctsw.recruit.utils.JwtUtil;
import com.ctsw.recruit.utils.Md5Util;
import com.ctsw.recruit.utils.ThreadLocalUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.netty.util.internal.StringUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.time.LocalDateTime;
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
    private StatesService statesService;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /*
        获取用户信息
        请求类型：GET
     */
    @GetMapping("/info")
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
        user.setUid((BigInteger) map.get("uid"));
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", user.getUid());
        userService.update(user, updateWrapper);
//        userService.update(user);
        return Result.success();
    }

    /*
        更新用户头像(
        Patch
     */
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
        BigInteger uid = (BigInteger) map.get("uid");
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
        BigInteger uid = (BigInteger) map.get("uid");
        QueryWrapper<Resume> wrapper = new QueryWrapper<Resume>()
                .eq("uid", uid);
        List<Resume> resumes = resumeService.list(wrapper);
        if(resumes.isEmpty()){
            return Result.error("没有简历");
        }
        return Result.success(resumes);
    }

    /*
        插入个人简历(每人拥有至多一份
        patch

     */
    @PostMapping("/upLoadResume")
    public Result upLoadResume(@RequestBody Resume resume){
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger uid = (BigInteger) map.get("uid");
        QueryWrapper<Resume> resumeQueryWrapper = new QueryWrapper<Resume>()
                .eq("uid", uid);
        resumeQueryWrapper.and(wrapper->wrapper
                .eq("resume_url", resume.getResumeUrl())
                .or()
                .eq("resume_name", resume.getResumeName()));
        if(resumeService.exists(resumeQueryWrapper)){
            return Result.error("存在相同名称或URL的简历");
        }
        resume.setId(null);
        resume.setUid(uid);
        resumeService.save(resume);
        return Result.success();
    }

    /*
        更新个人简历(每人拥有至多一份
        get

     */
    @PutMapping("/updateResume")
    public Result updateResume(@RequestBody @Validated Resume resume){
        if(resume.getId() == null || resume.getUid() == null){
            return Result.error("简历更新失败，简历id或对应的用户为空");
        }
        QueryWrapper<Resume> wrapper = new QueryWrapper<Resume>()
                .eq("id", resume.getId());
        resumeService.saveOrUpdate(resume, wrapper);
        return Result.success();
//        Resume resume = (Resume) getResume().getData();
//        if(resume == null){
//            return Result.error("没有简历");
//        }
//        Map<String, Object> map = ThreadLocalUtil.get();
//        String account = (String) map.get("account");
//        BigInteger uid = (BigInteger) map.get("uid");
//        userService.updateResume(uid, resumeUrl);
//        return Result.success();
    }

    @PutMapping("/apply")
    public Result apply(@RequestBody States states){
        states.setState(JobApplicationStateEnum.POST);  //设置状态
        states.setPostTime(LocalDateTime.now());    //设置时间
        QueryWrapper<States> wrapper = new QueryWrapper<>();
        wrapper.eq("position_id", states.getPositionId())
                .eq("userid", states.getUserid());
        States states1 = statesService.getOne(wrapper);
        if(states1 == null){
            statesService.save(states);
            return Result.success();
        }
        if(states1.getState()!=JobApplicationStateEnum.REJECT) return Result.error("您有一份正在审核中的简历");
        else statesService.update(states, wrapper);
        return Result.success();
    }

    //拿取申请信息
    @GetMapping("/applyStatus")
    public Result getApplyStatusList(){
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger uid = (BigInteger) map.get("uid");
        MPJLambdaWrapper<States> wrapper = new MPJLambdaWrapper<States>()
                .eq("userid", uid)
                .selectAll(States.class)
                .select(Position::getJobName, Position::getCity, Position::getPayroll, Position::getDegree)
                .leftJoin(Position.class, Position::getId, States::getPositionId)
                .select(Firm::getFirmCorp, Firm::getFirmTel, Firm::getFirmEmail, Firm::getFirmAvatarUrl)
                .leftJoin(Firm.class, Firm::getFirmUid, Position::getFirmid);
        List<StatesVO> statesVOs = statesService.selectJoinList(StatesVO.class, wrapper);
        return Result.success(statesVOs);
    }

    //删除申请信息
    @DeleteMapping("/withdrawApply")
    public Result withdrawApply(@RequestParam String positionId){
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger uid = (BigInteger) map.get("uid");
        QueryWrapper<States> wrapper = new QueryWrapper<States>()
                .eq("userid", uid)
                .eq("position_id", positionId);
        statesService.exists(wrapper);
        try {
            statesService.remove(wrapper);
        }catch (Exception e){
            return Result.error("此条目不存在");
        }
        return Result.success();
    }

    //删除
    @DeleteMapping("/removeResume")
    public Result removeResume(@RequestParam String resumeId){
        Map<String, Object> map = ThreadLocalUtil.get();
        BigInteger uid = (BigInteger) map.get("uid");
        QueryWrapper<Resume> wrapper = new QueryWrapper<Resume>()
                .eq("uid", uid)
                .eq("id", resumeId);
        resumeService.exists(wrapper);
        try {
            resumeService.remove(wrapper);
        }catch (Exception e){
            return Result.error("此条目不存在");
        }
        return Result.success();
    }

    //搜索工作
    @GetMapping("/positionListBySelect")
    public Result positionListBySelect(int selectType, String input){
        MPJLambdaWrapper<Position> queryWrapper = new MPJLambdaWrapper<>();
        queryWrapper.leftJoin(Firm.class, Firm::getFirmUid, Position::getFirmid);
        switch (selectType){
            case 1: queryWrapper.like("job_name", input); break;
            case 2: queryWrapper.like("city", input); break;
            case 3: queryWrapper.like("firm_corp", input); break;
            case 4: queryWrapper.like("degree", input);break;
        }
        queryWrapper.selectAll(Position.class)
                .select(Firm::getFirmCorp);
        List<Position> positionList = positionService.selectJoinList(Position.class, queryWrapper);
        return Result.success(positionList);
    }
}
