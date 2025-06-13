package com.ctsw.recruit.enumvalue;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum JobApplicationStateEnum {
    POST(0, "已发送"),
    READ(1, "已阅读"),
    ACCEPT(2, "已接受"),
    REJECT(3, "已拒绝");

    @EnumValue
    private final Integer stateCode;
    @JsonValue
    private final String state;

    JobApplicationStateEnum(int stateCode, String state){
        this.stateCode = stateCode;
        this.state = state;
    }
    public Integer getValue() {
        return stateCode;
    }

    @Override
    public String toString() {
        return state;
    }
}
