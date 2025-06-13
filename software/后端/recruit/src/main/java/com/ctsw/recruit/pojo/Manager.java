package com.ctsw.recruit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigInteger;

@Data
public class Manager {
    @TableId(type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger uid;
    private String account;
    private String uname;
    private String password;
    private String email;
    private String tel;
    @URL
    private String avatarUrl;
}
