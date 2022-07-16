package com.nju15.cloudnativeproject.controller;

import com.nju15.cloudnativeproject.annotation.Limit;
import com.nju15.cloudnativeproject.common.CommonResult;
import com.nju15.cloudnativeproject.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/hello")
public class HelloController {

    private final HelloService helloService;

    @Autowired
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/greet")
    @Limit(key = "greet",permitsPerSecond = 2, timeout = 10, timeunit = TimeUnit.MILLISECONDS,msg = "请求过于频繁,请稍后再试!")
    public Object greet(){
        return CommonResult.success(helloService.greet(),"请求成功");
    }

}
