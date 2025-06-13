package com.ctsw.recruit.enumvalue;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum SexEnum {
    MALE(0, "男"),
    FEMALE(1, "女")
    ;
    /**
     * 在需要保存到 数据库 的值上面加上注解
     */
    @EnumValue
    private final Integer sexCode;
    @JsonValue
    private final String sex;

    SexEnum(int sexCode, String sex){
        this.sexCode = sexCode;
        this.sex = sex;
    }
    public Integer getValue() {
        return sexCode;
    }

    @Override
    public String toString() {
        return sex;
    }
}
