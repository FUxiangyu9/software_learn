package com.ctsw.recruit.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

// User类
//lombok 在编译阶段，为实体类自动生产setter getter toString
@Data   //  与手动生成set，get，toString等方法效果相同
public class User {
    @NotNull
    private Integer uid;    //user id

    private String account; //user account

    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String uname;   //user name

    @JsonIgnore//让springmvc把当前对象转换成json字符串的时候,忽略password,最终的json字符串中就没有password这个属性了
    private String password;    //pwd

    private String introduction;    //user selfintroduction

    @NotEmpty
    @Email
    private String email;       //email

    private String     tel;        //telephone

    private String  avatarUrl;     //头像 地址链接

    private LocalDateTime   lastTime;   //  上次登录时间
}
