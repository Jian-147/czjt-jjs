package edu.czjtu.blackjack.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//统一响应结果
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String code;//业务状态码  0-成功  1-失败
    private String message;//提示信息
    private Object data;//响应数据

    //快速返回操作成功响应结果(带响应数据)
    public static  Result success(Object data) {
        Result result = success();
        result.setData(data);
        return result;
    }

    //快速返回操作成功响应结果
    public static Result success() {
        Result result = new Result();
        result.setCode("0");
        result.setMessage("请求成功！");
        return result;
    }


    public static Result error(String code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setMessage(message);
        return error("1",message);
    }
}
