package com.ctsw.recruit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.math.BigInteger;

@Data
public class Resume {
    @TableId(type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger id;
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger uid;
    private String resumeName;
    @URL
    private String resumeUrl;
    private String note;
}
