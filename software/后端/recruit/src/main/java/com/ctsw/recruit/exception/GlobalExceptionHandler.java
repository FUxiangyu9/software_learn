/*
*   GlobalExceptionHandler
*   作用：用于全局处理异常
*
 */

package com.ctsw.recruit.exception;

import com.ctsw.recruit.pojo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result handleException(Exception e){
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage())? e.getMessage():"操作失败");
                                                    //  如果异常消息不为空，则返回异常消息，否则返回操作失败
    }
}
