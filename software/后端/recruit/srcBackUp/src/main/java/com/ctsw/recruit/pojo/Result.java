package com.ctsw.recruit.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 统一响应结果
@NoArgsConstructor
@AllArgsConstructor
@Data   // 生产setter，getter方法。没有的话返回信息会出现status:406
public class Result<T> {
    private Integer code;   //业务状态码
    private String message; //提示信息
    private T data;         //响应数据

    //  快速返回操作成功的响应结果
    public static <E> Result<E> success(E data){
        return new Result<>(0, "操作成功", data);
    }
    public static Result<String> success(){
        return new Result<>(0, "操作成功", null);
    }
    public static Result<String> error(String message){
        return new Result<>(1, message, null);
    }
}
