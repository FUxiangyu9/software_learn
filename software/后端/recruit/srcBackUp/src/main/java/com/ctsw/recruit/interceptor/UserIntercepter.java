package com.ctsw.recruit.interceptor;

import com.ctsw.recruit.pojo.Manager;
import com.ctsw.recruit.utils.JwtUtil;
import com.ctsw.recruit.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;


//自定定义拦截器

@Component
public class UserIntercepter implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //令牌验证
        String token = request.getHeader("Authorization");
        //验证token(文件头携带)
        try{
            //从redis中获取相同的token
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            String redisToken = ops.get(token);
            if(redisToken == null){
                //token失效
                throw new RuntimeException();   //抛出异常
            }
            Map<String, Object> claims = JwtUtil.parseToken(token);
            //验证账号类型
            String role = (String)claims.get("role");
            if(role.equals("User")) {
                //把业务数据存储到ThreadLocal中
                ThreadLocalUtil.set(claims);
                return true;
            }
            else{
                response.setStatus(401);
                return false;
            }
        }catch (Exception e){
            //http状态响应码为401
            response.setStatus(401);
            //不放行
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{
        //清除ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
