package com.ctsw.recruit;

import com.ctsw.recruit.pojo.User;
import com.ctsw.recruit.service.UserService;
import com.ctsw.recruit.service.impl.UserServiceImpl;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class mybatis_user_Test {
    @Autowired
    UserService userService;
    @Test
    public void mybatis_user_Test() {
        List<User> list = userService.list();
        for (User user : list) {
            System.out.println(user);
        }
    }
}
