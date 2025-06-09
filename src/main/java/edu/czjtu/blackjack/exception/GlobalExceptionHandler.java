package edu.czjtu.blackjack.exception;


import edu.czjtu.blackjack.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常类
@ControllerAdvice("com.example.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody  //返回json串
    public Result handleCustomException(CustomException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody  //返回json串
    public Result handleException(Exception e) {
        return Result.error("1","系统异常！");
    }

}
