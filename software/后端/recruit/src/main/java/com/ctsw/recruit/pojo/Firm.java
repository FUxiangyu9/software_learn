package com.ctsw.recruit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigInteger;

@Data
public class Firm {
    @TableId(type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger firmUid;
    private String firmAccount;
    private String firmUname;
    private String firmCorp;
    private String password;
    @Email
    private String firmEmail;
    private String firmIntroduction;
    @URL
    private String firmAvatarUrl;
    private String firmTel;
}
