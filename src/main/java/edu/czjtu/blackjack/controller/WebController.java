package edu.czjtu.blackjack.controller;

import edu.czjtu.blackjack.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WebController {

    @GetMapping("/hello")
    public Result HelloWorld() {
        return Result.success("Hello World");
    }
    @GetMapping("/count")
    public Result count() {
        //throw new CustomException("400","错误，自定义错误禁止请求");
        int a = 100;
        return Result.success(a);
    }
    @GetMapping("/map")
    public Result map() {
        Map<String,Object> map = new HashMap<>();
        map.put("姓名","张三J");
        map.put("性别","男");
        map.put("年龄","99");
        return Result.success(map);
    }
}
