package com.ctsw.recruit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigInteger;

@Data
public class Position {
    @TableId(type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger id;
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger firmid;
    private String jobName;
    private String city;
    private String degree;
    private String request;
    private int hc;
    private int payroll;
    private String location;
    @TableField(exist = false)
    private String firmCorp;
    @TableField(exist = false)
    private Integer deliveryNum;
    @TableField(exist = false)
    private Integer passNum;
}
