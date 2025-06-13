package com.ctsw.recruit.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ctsw.recruit.pojo.States;
import com.github.yulichang.annotation.EntityMapping;
import lombok.Data;

@Data
public class StatesVO extends States {
    //position
    @TableField(exist = false)
    private String jobName;
    @TableField(exist = false)
    private String city;
    @TableField(exist = false)
    private String payroll;
    @TableField(exist = false)
    private String degree;

    //firm
    @TableField(exist = false)
    private String firmCorp;
    @TableField(exist = false)
    private String firmTel;
    @TableField(exist = false)
    private String firmEmail;

    //user
    @TableField(exist = false)
    private String uname;
}
