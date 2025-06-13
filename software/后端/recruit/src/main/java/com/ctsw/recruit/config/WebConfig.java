package com.ctsw.recruit.config;

import com.ctsw.recruit.interceptor.FirmIntercepter;
import com.ctsw.recruit.interceptor.LoginIntercepter;
import com.ctsw.recruit.interceptor.ManagerIntercepter;
import com.ctsw.recruit.interceptor.UserIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration  //注入到配置类中

public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private UserIntercepter userIntercepter;
    @Autowired
    private FirmIntercepter firmIntercepter;
    @Autowired
    private ManagerIntercepter managerIntercepter;
    @Autowired
    private LoginIntercepter loginIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
//        //登录接口和注册接口不拦截
//        registry.addInterceptor(loginInterceptor).excludePathPatterns(
//                "/userLogin",
//                "/firmLogin",
//                "/managerLogin",
//                "/register/userReg",
//                "/register/firmReg"
//        );
        registry.addInterceptor(userIntercepter).addPathPatterns(
                "/user/**"
        );
        registry.addInterceptor(firmIntercepter).addPathPatterns(
                "/firm/**"
        );
        registry.addInterceptor(managerIntercepter).addPathPatterns(
                "/manager/**"
        );
        registry.addInterceptor(loginIntercepter).addPathPatterns(
                "/resume/**",
                "/upload",
                "/position/**"
        );
    }
}
